package com.dasher.osugdx.Framework.Tasks;

import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Timer;

// https://stackoverflow.com/questions/39128425/libgdx-is-performance-effected-by-creating-new-timer-task-every-frame
public abstract class PoolTask extends Timer.Task {
    public void run (){
        doTask();
        Pools.free(this);
    }

    abstract public void doTask();
}