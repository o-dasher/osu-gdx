package com.dasher.osugdx.GameScenes.Gameplay;

public class GameplayUtils {
    /**
     * Maps a difficulty value to the given range.
     * @param difficulty the difficulty value
     * @param min the min
     * @param mid the mid
     * @param max the max
     * @author peppy (ppy/osu-iPhone:OsuFunctions.m)
     */
    public static float mapDifficultyRange(float difficulty, float min, float mid, float max) {
        if (difficulty > 5f)
            return mid + (max - mid) * (difficulty - 5f) / 5f;
        else if (difficulty < 5f)
            return mid - (mid - min) * (5f - difficulty) / 5f;
        else
            return mid;
    }
}
