package com.dasher.osugdx.assets;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AssetPaths {
    @Contract(pure = true)
    public static @NotNull String getBase() { return "assets/"; }
    protected final TexturesPath textures = new TexturesPath();
    protected final SoundsPath soundsPath = new SoundsPath();
    protected final String musicsPath = "Musics/";
    protected final String fontsPath = "Fonts/";
    protected final String introPath = "Intro/";
    protected final String menuPath = "Menu/";
}


