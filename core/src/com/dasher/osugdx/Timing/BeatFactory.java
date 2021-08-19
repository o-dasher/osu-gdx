package com.dasher.osugdx.Timing;

import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.Framework.Interfaces.UpdateAble;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManager;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class BeatFactory implements Listenable<BeatListener>, UpdateAble, BeatListener, BeatmapManagerListener {
    private final BeatmapManager beatmapManager;
    private final Array<BeatListener> listeners = new Array<>();

    public BeatFactory(BeatmapManager beatmapManager) {
        this.beatmapManager = beatmapManager;
    }

    @Override
    public Array<BeatListener> getListeners() {
        return listeners;
    }

    private int currentTimingPointIndex = 0;
    private TimingPoint currentTimingPoint = null;
    private long timeSinceLastBeat = 0;

    @Override
    public void update(float delta) {
        Beatmap beatmap = beatmapManager.getCurrentMap();

        if (beatmap == null) {
            return;
        }

        if (timeSinceLastBeat == 0) {
            timeSinceLastBeat = beatmapManager.getTimeLastMap();
        }

        Array<TimingPoint> timingPoints = beatmapManager.getCurrentMap().getTimingPoints();
        double deltaSinceLastMap = (System.nanoTime() - beatmapManager.getTimeLastMap()) / 1e6;

        if (currentTimingPoint == null) {
            for (TimingPoint timingPoint: timingPoints) {
                if (!timingPoint.isInherited()) {
                    currentTimingPoint = timingPoint;
                }
            }
        }

        while (currentTimingPointIndex < timingPoints.size - 1 && timingPoints.get(currentTimingPointIndex + 1).getTimestamp() <= deltaSinceLastMap) {
            currentTimingPointIndex++;
        }

        TimingPoint newTimingPoint = timingPoints.get(currentTimingPointIndex);
        if (!newTimingPoint.isInherited()) {
            currentTimingPoint = newTimingPoint;
        }

        double beatLength = currentTimingPoint.getBeatLength();
        double timePassedSinceLastBeat = (System.nanoTime() - timeSinceLastBeat) / 1e6;

        if (timePassedSinceLastBeat > beatLength) {
            timeSinceLastBeat = System.nanoTime();
            onNewBeat(currentTimingPoint);
        }
    }

    public TimingPoint getBeatFor(@NotNull BeatListener beatListener, @NotNull TimingPoint timingPoint) {
        TimingPoint clone = timingPoint.clone();
        clone.setBeatLength(timingPoint.getBeatLength() / beatListener.getBeatDivisor());
        return clone;
    }

    @Override
    public void onNewBeat(TimingPoint timingPoint) {
        for (BeatListener listener: getListeners()) {
            listener.onNewBeat(getBeatFor(listener, timingPoint));
        }
    }

    @Override
    public void onFourthBeat(TimingPoint timingPoint) {
        for (BeatListener listener: getListeners()) {
            listener.onNewBeat(getBeatFor(listener, timingPoint));
        }
    }

    @Override
    public double getBeatDivisor() {
        return 1;
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {
        currentTimingPointIndex = 0;
        currentTimingPoint = null;
        timeSinceLastBeat = 0;
    }
}
