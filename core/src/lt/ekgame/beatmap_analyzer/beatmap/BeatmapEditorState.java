package lt.ekgame.beatmap_analyzer.beatmap;

import com.badlogic.gdx.utils.IntArray;

import java.util.Arrays;

import lt.ekgame.beatmap_analyzer.parser.BeatmapException;
import lt.ekgame.beatmap_analyzer.parser.FilePart;
import lt.ekgame.beatmap_analyzer.parser.FilePartConfig;

public class BeatmapEditorState {
	
	private IntArray bookmarks = new IntArray();
	private double distanceSpacing;
	private int beatDivisor;
	private int gridSize;
	private double timelineZoom;
	
	private BeatmapEditorState() {}

	public BeatmapEditorState(FilePart part) throws BeatmapException {
		FilePartConfig config = new FilePartConfig(part);
		distanceSpacing = config.getDouble("DistanceSpacing");
		beatDivisor = config.getInt("BeatDivisor");
		gridSize = config.getInt("GridSize");
		timelineZoom = config.getDouble("TimelineZoom", 1);
		
		if (config.hasProperty("Bookmarks")) {
			Integer[] bookmarksArray = Arrays
					.stream(config.getString("Bookmarks").split(","))
					.filter(o->!o.isEmpty())
					.map(o->Integer.parseInt(o.trim()))
					.toArray(Integer[]::new);
			for (int bookmark : bookmarksArray) {
				bookmarks.add(bookmark);
			}
		}
	}
	
	public BeatmapEditorState clone() {
		BeatmapEditorState clone = new BeatmapEditorState();
		clone.distanceSpacing = this.distanceSpacing;
		clone.beatDivisor = this.beatDivisor;
		clone.gridSize = this.gridSize;
		clone.timelineZoom = this.timelineZoom;
		clone.bookmarks = this.bookmarks;
		return clone;
	}

	public IntArray getBookmarks() {
		return bookmarks;
	}

	public double getDistanceSpacing() {
		return distanceSpacing;
	}

	public int getBeatDivisor() {
		return beatDivisor;
	}

	public int getGridSize() {
		return gridSize;
	}

	public double getTimelineZoom() {
		return timelineZoom;
	}

	public void setBookmarks(IntArray bookmarks) {
		this.bookmarks = bookmarks;
	}

	public void setDistanceSpacing(double distanceSpacing) {
		this.distanceSpacing = distanceSpacing;
	}

	public void setBeatDivisor(int beatDivisor) {
		this.beatDivisor = beatDivisor;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public void setTimelineZoom(double timelineZoom) {
		this.timelineZoom = timelineZoom;
	}
}
