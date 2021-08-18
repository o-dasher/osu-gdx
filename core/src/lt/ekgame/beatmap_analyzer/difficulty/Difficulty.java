package lt.ekgame.beatmap_analyzer.difficulty;

import com.badlogic.gdx.utils.Array;

import java.util.List;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.performance.Performance;
import lt.ekgame.beatmap_analyzer.performance.scores.Score;
import lt.ekgame.beatmap_analyzer.utils.Mod;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public abstract class Difficulty {
	
	protected Beatmap beatmap;
	protected Mods mods;
	protected double starDiff;
	protected Array<Double> strains;
	
	public Difficulty(Beatmap beatmap, Mods mods, double starDiff, Array<Double> strains) {
		this.beatmap = beatmap;
		this.mods = mods;
		this.starDiff = starDiff;
		this.strains = strains;
	}
	
	public abstract Performance getPerformance(Score score);
	
	public Array<Double> getStrains() {
		return strains;
	}
	
	public double getSpeedMultiplier() {
		return mods.getSpeedMultiplier();
	}
	
	public double getOD() {
		return beatmap.getDifficultySettings().getOD();
	}
	
	public Beatmap getBeatmap() {
		return beatmap;
	}
	
	public Mods getMods() {
		return mods;
	}

	public double getStars() {
		return starDiff;
	}

	public int getMaxCombo() {
		return beatmap.getMaxCombo();
	}
	
	public int getObjectCount() {
		return beatmap.getObjectCount();
	}
	
	public boolean hasMod(Mod mod) {
		return mods.has(mod);
	}
}
