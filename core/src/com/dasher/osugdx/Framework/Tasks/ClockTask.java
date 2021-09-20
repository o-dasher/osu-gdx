package com.dasher.osugdx.Framework.Tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pools;

public abstract class ClockTask {
    private boolean isCancelled = false;
    private boolean isRepeating = false;
    private float timeSeconds = 0f;
    private float period;

    public ClockTask(float period) {
        this.period = period;
    }

    public void update() {
        if (!isCancelled) {
            timeSeconds += Gdx.graphics.getDeltaTime();
            if (!isWaiting()) {
                run();
                if (isRepeating) {
                    timeSeconds -= period;
                } else {
                    cancel();
                }
            }
        }
    }

    public boolean isWaiting() {
        return timeSeconds < period;
    }

    public abstract void run();

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public void cancel() {
        isCancelled = true;
        timeSeconds = 0;
        period = 0;
        Pools.free(this);
    }
}
