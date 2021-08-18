package lt.ekgame.beatmap_analyzer.beatmap.mania;

import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.List;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapDifficulties;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapEditorState;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapGenerals;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;
import lt.ekgame.beatmap_analyzer.difficulty.ManiaDifficulty;
import lt.ekgame.beatmap_analyzer.difficulty.ManiaDifficultyCalculator;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public class ManiaBeatmap extends Beatmap {
	
	private Array<ManiaObject> hitObjects;

	public ManiaBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState, BeatmapMetadata metadata,
			BeatmapDifficulties difficulties, Array<BreakPeriod> breaks, Array<TimingPoint> timingPoints,
			Array<ManiaObject> hitObjects) {
		super(generals, editorState, metadata, difficulties, breaks, timingPoints);
		this.hitObjects = hitObjects;
		
		finalizeObjects(hitObjects);
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.MANIA;
	}
	
	@Override
	public ManiaDifficultyCalculator getDifficultyCalculator() {
		return new ManiaDifficultyCalculator();
	}

	@Override
	public ManiaDifficulty getDifficulty(Mods mods) {
		return getDifficultyCalculator().calculate(mods, this);
	}
	
	@Override
	public ManiaDifficulty getDifficulty() {
		return getDifficulty(Mods.NOMOD);
	}
	
	public Array<ManiaObject> getHitObjects() {
		return hitObjects;
	}
	
	public int getCollumns() {
		return (int)difficulties.getCS();
	}

	@Override
	public int getMaxCombo() {
		return Arrays.stream(hitObjects.items).mapToInt(HitObject::getCombo).sum();
	}
	
	@Override
	public int getObjectCount() {
		return hitObjects.size;
	}
}
