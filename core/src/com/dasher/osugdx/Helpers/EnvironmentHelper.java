package com.dasher.osugdx.Helpers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EnvironmentHelper {
    @Contract(pure = true)
    public static @NotNull String getAndroidExternalStoragePath() {
        return "/sdcard/";
    }
}
