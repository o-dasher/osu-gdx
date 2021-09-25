package lt.ekgame.beatmap_analyzer.beatmap;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.difficulty.Difficulty;
import lt.ekgame.beatmap_analyzer.difficulty.DifficultyCalculator;
import lt.ekgame.beatmap_analyzer.utils.Mods;

public abstract class Beatmap implements Cloneable {
	public String beatmapFilePath = "";
	protected BeatmapGenerals generals;
	protected BeatmapEditorState editorState;
	protected BeatmapMetadata metadata;
	protected BeatmapDifficulties difficulties;
	protected Array<BreakPeriod> breaks;
	protected Array<TimingPoint> timingPoints;
	private boolean calculatedBaseDiff;  // Easy queries.
	protected double baseStars = 0;
	protected double baseCombo = 0;

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

	public void calculateBase() {
		if (!calculatedBaseDiff) {
			Difficulty baseDifficulty = getDifficulty();
			baseStars = baseDifficulty.getStars();
			baseCombo = baseDifficulty.getMaxCombo();
			calculatedBaseDiff = true;
		}
	}

	public void freeResources() {
		if (getHitObjects() != null) {
			calculateBase();
			getHitObjects().clear();
		}
		for (BreakPeriod breakPeriod: breaks) {
			if (!breakPeriod.isBackground()) {
				breaks.removeValue(breakPeriod, true);
			}
		}
		timingPoints = null;
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
		if (timingPoints.isEmpty()) {
			return;
		}
		if (objects.isEmpty()) {
			return;
		}

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

	public double getBaseCombo() {
		return baseCombo;
	}

	public double getBaseStars() {
		return baseStars;
	}

	public void setMetadata(BeatmapMetadata metadata) {
		this.metadata = metadata;
	}

	public void setGenerals(BeatmapGenerals generals) {
		this.generals = generals;
	}

	public void setDifficulties(BeatmapDifficulties difficulties) {
		this.difficulties = difficulties;
	}

	public void setEditorState(BeatmapEditorState editorState) {
		this.editorState = editorState;
	}

	public void setBaseCombo(double baseCombo) {
		this.baseCombo = baseCombo;
	}

	public void setBaseStars(double baseStars) {
		this.baseStars = baseStars;
	}

	public void setTimingPoints(Array<TimingPoint> timingPoints) {
		this.timingPoints = timingPoints;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
