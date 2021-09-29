package lt.ekgame.beatmap_analyzer.difficulty;

import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.mania.ManiaBeatmap;
import lt.ekgame.beatmap_analyzer.beatmap.mania.ManiaObject;
import lt.ekgame.beatmap_analyzer.utils.MathUtils;
import lt.ekgame.beatmap_analyzer.utils.Mods;
import lt.ekgame.beatmap_analyzer.utils.Quicksort;

public class ManiaDifficultyCalculator implements DifficultyCalculator {

	public static final double STAR_SCALING_FACTOR = 0.018;

	public static final double INDIVIDUAL_DECAY_BASE = 0.125;
	public static final double OVERALL_DECAY_BASE = 0.3;

	public static final double DECAY_WEIGHT = 0.9;
	public static final int STRAIN_STEP = 400;

	public ManiaDifficulty calculate(Mods mods, Beatmap beatmap) {
		ManiaBeatmap bm = (ManiaBeatmap) beatmap;
		double timeRate = mods.getSpeedMultiplier();
		Array<ManiaObject> hitObjects = bm.getHitObjects();
		Array<DifficultyObject> objects = generateDifficultyObjects(hitObjects, timeRate, bm.getCollumns());
		Array<Double> strains = calculateStrains(objects, timeRate);
		Array<Double> originalStrains = new Array<>(strains);
		double difficulty = calculateDifficulty(strains);
		return new ManiaDifficulty(bm, mods, difficulty, originalStrains);
	}

	private Array<DifficultyObject> generateDifficultyObjects(Array<ManiaObject> hitObjects, double timeRate,
			int collumns) {
		Array<DifficultyObject> difficultyObjects = new Array<>();

		for (ManiaObject hitObject: hitObjects) {
			DifficultyObject difficultyObject = new DifficultyObject(hitObject, collumns);
			difficultyObjects.add(difficultyObject);
		}

		Quicksort.sort(difficultyObjects);
		// .NET 4.0 framework uses quicksort - an unstable algorithm, which means
		// that objects with equal value may change order.
		// This algorithm depends on the order of the difficulty objects.
		// Mania has lots of notes on the same timestamp.
		// This is still not precise and might produce about 0.05 error for the final result.
		// Because of this error, the current highest PP score is calculated
		// as if it is worth 7.73pp more (actual: 1209.12pp, calculated: 1216.85)

		DifficultyObject previous = null;
		for (DifficultyObject current : difficultyObjects) {
			if (previous != null)
				current.calculateStrains(previous, timeRate, collumns);
			previous = current;
		}

		return difficultyObjects;
	}

	private Array<Double> calculateStrains(Array<DifficultyObject> difficultyObjects, double timeRate) {
		Array<Double> highestStrains = new Array<>();
		double realStrainStep = STRAIN_STEP * timeRate;
		double intervalEnd = realStrainStep;
		double maxStrain = 0;

		DifficultyObject previous = null;
		for (DifficultyObject current : difficultyObjects) {
			while (current.object.getStartTime() > intervalEnd) {
				highestStrains.add(maxStrain);
				if (previous != null) {
					double individualDecay = Math.pow(INDIVIDUAL_DECAY_BASE,
							(intervalEnd - previous.object.getStartTime()) / 1000.0);
					double overallDecay = Math.pow(OVERALL_DECAY_BASE,
							(intervalEnd - previous.object.getStartTime()) / 1000.0);
					maxStrain = previous.individualStrains[previous.collumn] * individualDecay
							+ previous.strain * overallDecay;
				}
				intervalEnd += realStrainStep;
			}
			maxStrain = Math.max(maxStrain, current.individualStrains[current.collumn] + current.strain);
			previous = current;
		}
		return highestStrains;
	}

	public double calculateDifficulty(Array<Double> strains) {
		strains.sort((a, b) -> b.compareTo(a));
		double difficulty = 0, weight = 1;
		for (double strain : strains) {
			difficulty += weight * strain;
			weight *= DECAY_WEIGHT;
		}
		return difficulty * STAR_SCALING_FACTOR;
	}

	static class DifficultyObject implements Comparable<DifficultyObject> {

		@Override
		public int compareTo(DifficultyObject o) {
			return (int) (object.getStartTime() - o.object.getStartTime());
		}

		private final ManiaObject object;
		private final int collumn;

		private double strain = 1;
		private final double[] individualStrains;
		private final double[] heldUntil;

		DifficultyObject(ManiaObject object, int collumns) {
			this.object = object;

			individualStrains = new double[collumns];
			heldUntil = new double[collumns];
			collumn = MathUtils.calculateManiaCollumn(object.getPosition().x, collumns);
		}

		private void calculateStrains(DifficultyObject previous, double timeRate, int collumns) {
			double timeElapsed = (object.getStartTime() - previous.object.getStartTime()) / timeRate;
			double individualDecay = Math.pow(INDIVIDUAL_DECAY_BASE, timeElapsed / 1000);
			double overallDecay = Math.pow(OVERALL_DECAY_BASE, timeElapsed / 1000);
			double holdFactor = 1;
			double holdAddition = 0;

			for (int i = 0; i < collumns; i++) {
				heldUntil[i] = previous.heldUntil[i];

				if (object.getStartTime() < heldUntil[i] && object.getEndTime() > heldUntil[i]
						&& object.getEndTime() != heldUntil[i])
					holdAddition = 1;

				if (heldUntil[i] > object.getEndTime())
					holdFactor = 1.25;

				individualStrains[i] = previous.individualStrains[i] * individualDecay;
			}

			heldUntil[collumn] = object.getEndTime();
			individualStrains[collumn] += 2 * holdFactor;
			strain = previous.strain * overallDecay + (1 + holdAddition) * holdFactor;
		}
	}
}
