package lt.ekgame.beatmap_analyzer.difficulty;

import com.badlogic.gdx.utils.Array;

import java.util.List;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public interface DifficultyCalculator {
	
	Difficulty calculate(Mods mods, Beatmap beatmap);
	
	double calculateDifficulty(Array<Double> strains);

}
