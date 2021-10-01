package com.dasher.osugdx.Timing;

import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.Framework.Interfaces.UpdateAble;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public class BeatFactory implements Listenable<BeatListener>, UpdateAble, BeatListener, BeatmapManagerListener {
    private final BeatmapManager beatmapManager;
    private final Array<BeatListener> listeners = new Array<>();
    private ClockTask clockTask;
    private OsuGame game;

    public BeatFactory(BeatmapManager beatmapManager, OsuGame game) {
        this.beatmapManager = beatmapManager;
        this.game = game;
    }

    @Override
    public Array<BeatListener> getListeners() {
        return listeners;
    }

    private int currentTimingPointIndex = 0;
    private TimingPoint currentTimingPoint = null;
    private long timeSinceLastBeat = 0;
    private int beatAccumulator = 0;

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

        if (timingPoints == null || timingPoints.isEmpty()) {
            System.out.println("Ignoring timing points of beatmap with no timing points.");
            return;
        }

        if (currentTimingPoint == null) {
            for (TimingPoint timingPoint: timingPoints) {
                if (!timingPoint.isInherited()) {
                    currentTimingPoint = timingPoint;
                }
            }
        }

        while (currentTimingPointIndex < timingPoints.size - 1 && timingPoints.get(currentTimingPointIndex + 1).getTimestamp() <= game.currentMusicPosition * 1000) {
            currentTimingPointIndex++;
        }

        TimingPoint newTimingPoint;
        if (currentTimingPointIndex < timingPoints.size) {
            newTimingPoint = timingPoints.get(currentTimingPointIndex);
        } else {
            return;
        }

        if (!newTimingPoint.isInherited()) {
            currentTimingPoint = newTimingPoint;
        }

        double beatLength = currentTimingPoint.getBeatLength();

        if (clockTask != null) {
            clockTask.update(delta);
        }

        if (clockTask == null || clockTask.isCancelled()) {
            clockTask = new ClockTask((float) (beatLength / 1000)) {
                @Override
                public void run() {
                    if (beatAccumulator % 4 == 0) {
                        onFourthBeat(currentTimingPoint);
                    } else {
                        onNewBeat(currentTimingPoint);
                    }
                    beatAccumulator++;
                }
            };
        }
    }

    public TimingPoint getBeatFor(@NotNull BeatListener beatListener, @NotNull TimingPoint timingPoint) {
        TimingPoint clone = timingPoint.clone();
        clone.setBeatLength(
                Math.max(
                        beatListener.getMinimalBeatLength(),
                        clone.getBeatLength() / beatListener.getBeatDivisor()
                )
        );
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
            listener.onFourthBeat(getBeatFor(listener, timingPoint));
        }
    }

    @Override
    public double getBeatDivisor() {
        return 1;
    }

    @Override
    public void onNewBeatmap(Beatmap beatmap) {

    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {

    }

    @Override
    public void onPreBeatmapChange() {
        currentTimingPointIndex = 0;
        currentTimingPoint = null;
        timeSinceLastBeat = 0;
        beatAccumulator = 0;
    }
}
