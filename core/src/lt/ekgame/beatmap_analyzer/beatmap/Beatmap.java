package lt.ekgame.beatmap_analyzer.beatmap;

import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.difficulty.Difficulty;
import lt.ekgame.beatmap_analyzer.difficulty.DifficultyCalculator;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public abstract class Beatmap {
	public String beatmapFilePath = "";
	protected BeatmapGenerals generals;
	protected BeatmapEditorState editorState;
	protected BeatmapMetadata metadata;
	protected BeatmapDifficulties difficulties;
	protected Array<BreakPeriod> breaks;
	protected Array<TimingPoint> timingPoints;

	protected Beatmap(BeatmapGenerals generals, BeatmapEditorState editorState,
		BeatmapMetadata metadata, BeatmapDifficulties difficulties,
		Array<BreakPeriod> breaks, Array<TimingPoint> timingPoints)
	{
		this.generals = generals;
		this.editorState = editorState;
		this.metadata = metadata;
		this.difficulties = difficulties;
		this.breaks = breaks;
		this.timingPoints = timingPoints;
	}

	public void freeResources() {
		for (BreakPeriod breakPeriod: breaks) {
			if (!breakPeriod.isBackground()) {
				breaks.removeValue(breakPeriod, true);
			}
		}
		editorState = null;
	}

	public boolean isResourcesFree() {
		return
			timingPoints.isEmpty()
				&& getHitObjects().isEmpty()
				&& getBreaks().isEmpty()
				&& getEditorState() == null
				&& getGenerals() == null;
	}
	
	protected void finalizeObjects(Array<? extends HitObject> objects) {
		List<TimingPoint> timingPointList = new ArrayList<>();
		List<HitObject> objectsList = new ArrayList<>();

		for (TimingPoint tp: timingPoints) {
			timingPointList.add(tp);
		}
		for (HitObject ho: objects) {
			objectsList.add(ho);
		}

		ListIterator<TimingPoint> timingIterator = timingPointList.listIterator();
		ListIterator<HitObject> objectIterator = objectsList.listIterator();
		
		// find first parent point
		TimingPoint parent = null;
		while (parent == null || parent.isInherited())
			parent = timingIterator.next();
		
		while (true) {
			TimingPoint current = timingIterator.hasNext() ? timingIterator.next() : null;
			TimingPoint previous = timingPoints.get(timingIterator.previousIndex() - (current == null ? 0 : 1));
			if (!previous.isInherited()) parent = previous;
			
			while (objectIterator.hasNext()) {
				HitObject object = objectIterator.next();
				if (current == null || object.getStartTime() < current.getTimestamp()) {
					object.finalize(previous, parent, this);
				}
				else {
					objectIterator.previous();
					break;
				}	
			}
			
			if (current == null) break;
		}
	}
	
	public abstract GameMode getGamemode();
	
	public abstract DifficultyCalculator getDifficultyCalculator();
	
	public abstract Difficulty getDifficulty(Mods mods);
	
	public abstract Difficulty getDifficulty();
	
	public abstract int getMaxCombo();
	
	public abstract int getObjectCount();

	public abstract Array<? extends HitObject> getHitObjects();

	public BeatmapGenerals getGenerals() {
		return generals;
	}

	public BeatmapEditorState getEditorState() {
		return editorState;
	}

	public BeatmapMetadata getMetadata() {
		return metadata;
	}

	public BeatmapDifficulties getDifficultySettings() {
		return difficulties;
	}

	public Array<BreakPeriod> getBreaks() {
		return breaks;
	}
	
	public Array<TimingPoint> getTimingPoints() {
		return timingPoints;
	}

	@Override
	public String toString() {
		return
				getMetadata().getArtist()
				+ " // " + getMetadata().getCreator()
				+ " - " + getMetadata().getTitle()
				+ " [" + getMetadata().getVersion() + "] ";
	}
}
