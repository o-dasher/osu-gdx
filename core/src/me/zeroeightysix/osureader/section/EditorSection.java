package me.zeroeightysix.osureader.section;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
public class EditorSection extends Section {

    @Source("Bookmarks") List bookmars;
    @Source("DistanceSpacing") double distanceSpacing;
    @Source("BeatDivisor") int beatDivisor;
    @Source("GridSize") int gridSize;
    @Source("TimelineZoom") int timelineZoom;

    public EditorSection(OsuSection source) throws IllegalAccessException {
        super(source);
    }

    public int getBeatDivisor() {
        return beatDivisor;
    }

    public double getDistanceSpacing() {
        return distanceSpacing;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getTimelineZoom() {
        return timelineZoom;
    }

    public List<Integer> getBookmars() {
        if (bookmars == null) return null;
        return bookmars.stream().mapToInt(o -> (int)Double.parseDouble(o.toString())).boxed().collect(Collectors.toList());
    }
}
