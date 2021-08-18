package lt.ekgame.beatmap_analyzer.beatmap.ctb;

import com.badlogic.gdx.utils.Array;

import java.util.List;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapDifficulties;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapEditorState;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapGenerals;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;
import lt.ekgame.beatmap_analyzer.difficulty.Difficulty;
import lt.ekgame.beatmap_analyzer.difficulty.DifficultyCalculator;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public class CatchBeatmap extends Beatmap {
	
	private Array<CatchObject> hitObjects;

	private CatchBeatmap() {
		super(null, null, null, null, null, null);
	}

	public CatchBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState, BeatmapMetadata metadata,
			BeatmapDifficulties difficulties, Array<BreakPeriod> breaks, Array<TimingPoint> timingPoints,
			Array<CatchObject> hitObjects) {
		super(generals, editorState, metadata, difficulties, breaks, timingPoints);
		this.hitObjects = hitObjects;
		
		finalizeObjects(hitObjects);
	}
	
	public Array<CatchObject> getHitObjects() {
		return hitObjects;
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.CATCH;
	}

	@Override
	public Difficulty getDifficulty(Mods mods) {
		return null;
	}

	@Override
	public Difficulty getDifficulty() {
		return null;
	}
	
	@Override
	public DifficultyCalculator getDifficultyCalculator() {
		return null;
	}

	@Override
	public int getMaxCombo() {
		return 0;
	}

	@Override
	public int getObjectCount() {
		return 0;
	}
}
