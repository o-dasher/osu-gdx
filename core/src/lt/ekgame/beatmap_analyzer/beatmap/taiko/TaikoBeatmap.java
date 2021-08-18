package lt.ekgame.beatmap_analyzer.beatmap.taiko;

import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.List;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.*;
import lt.ekgame.beatmap_analyzer.difficulty.TaikoDifficulty;
import lt.ekgame.beatmap_analyzer.difficulty.TaikoDifficultyCalculator;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public class TaikoBeatmap extends Beatmap {
	
	private Array<TaikoObject> hitObjects;

	public TaikoBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState, BeatmapMetadata metadata,
			BeatmapDifficulties difficulties, Array<BreakPeriod> breaks, Array<TimingPoint> timingPoints,
			Array<TaikoObject> hitObjects) {
		super(generals, editorState, metadata, difficulties, breaks, timingPoints);
		this.hitObjects = hitObjects;
		finalizeObjects(hitObjects);
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.TAIKO;
	}

	@Override
	public int getMaxCombo() {
		return (int) Arrays.stream(hitObjects.items).filter(o->o instanceof TaikoCircle).count();
	}
	
	public Array<TaikoObject> getHitObjects() {
		return hitObjects;
	}

	@Override
	public int getObjectCount() {
		return hitObjects.size;
	}
	
	@Override
	public TaikoDifficultyCalculator getDifficultyCalculator() {
		return new TaikoDifficultyCalculator();
	}

	@Override
	public TaikoDifficulty getDifficulty(Mods mods) {
		return getDifficultyCalculator().calculate(mods, this);
	}

	@Override
	public TaikoDifficulty getDifficulty() {
		return getDifficulty(Mods.NOMOD);
	}
}
