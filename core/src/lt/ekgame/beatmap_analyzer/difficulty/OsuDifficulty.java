package lt.ekgame.beatmap_analyzer.difficulty;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuBeatmap;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuCircle;
import lt.ekgame.beatmap_analyzer.performance.OsuPerformanceCalculator;
import lt.ekgame.beatmap_analyzer.performance.Performance;
import lt.ekgame.beatmap_analyzer.performance.scores.Score;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public class OsuDifficulty extends Difficulty {
	
	private double aimDiff, speedDiff;
	
	private Array<Double> aimStrains;
	private Array<Double> speedStrains;
	
	public OsuDifficulty(OsuBeatmap beatmap, Mods mods, double starDiff, double aimDiff, double speedDiff, Array<Double> aimStrains, Array<Double> speedStrains) {
		super(beatmap, mods, starDiff, mergeStrains(aimStrains, speedStrains));
		this.aimDiff = aimDiff;
		this.speedDiff = speedDiff;
		this.aimStrains = aimStrains;
		this.speedStrains = speedStrains;
	}
	
	private static Array<Double> mergeStrains(Array<Double> aimStrains, Array<Double> speedStrains) {
		Array<Double> overall = new Array<Double>();
		Iterator<Double> aimIterator = aimStrains.iterator();
		Iterator<Double> speedIterator = speedStrains.iterator();
		while (aimIterator.hasNext() && speedIterator.hasNext()) {
			Double aimStrain = aimIterator.next();
			Double speedStrain = speedIterator.next();
			overall.add(aimStrain + speedStrain + Math.abs(speedStrain - aimStrain)*OsuDifficultyCalculator.EXTREME_SCALING_FACTOR);
		}
		return overall;
	}
	
	public Array<Double> getAimStrains() {
		return aimStrains;
	}
	
	public Array<Double> getSpeedStrains() {
		return speedStrains;
	}

	public double getAim() {
		return aimDiff;
	}

	public double getSpeed() {
		return speedDiff;
	}
	
	public double getAR() {
		return ((OsuBeatmap)beatmap).getAR(mods);
	}

	public double getOD() {
		return ((OsuBeatmap)beatmap).getOD(mods);
	}
	
	public int getNumCircles() {
		return (int) Arrays.stream(((OsuBeatmap) beatmap).getHitObjects().items).filter(o->o instanceof OsuCircle).count();
	}

	@Override
	public Performance getPerformance(Score score) {
		return new OsuPerformanceCalculator().calculate(this, score);
	}
}
