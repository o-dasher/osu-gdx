package com.dasher.osugdx.Framework.Tasks;


import com.dasher.osugdx.Framework.Interfaces.UpdateAble;

public abstract class ClockTask implements UpdateAble {
    private boolean isCancelled = false;
    private boolean isRepeating = false;
    private float timeSeconds = 0f;
    private float period;

    public ClockTask(float period) {
        this.period = period;
    }

    @Override
    public void update(float delta) {
        if (!isCancelled) {
            timeSeconds += delta;
            if (timeSeconds > period) {
                run();
                if (isRepeating) {
                    timeSeconds -= period;
                } else {
                    cancel();
                }
            }
        }
    }

    public abstract void run();

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public void cancel() {
        isCancelled = true;
        timeSeconds = 0;
        period = 0;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}
