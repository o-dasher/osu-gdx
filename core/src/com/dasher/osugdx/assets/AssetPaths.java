package com.dasher.osugdx.assets;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AssetPaths {
    @Contract(pure = true)
    public static @NotNull String getBase() { return "assets/"; }
    public final TexturesPath textures = new TexturesPath();
    public final SoundsPath soundsPath = new SoundsPath();
    public final FontsPath fontsPath = new FontsPath();
    public final String musicsPath = "Musics/";
    public final String introPath = "Intro/";
    public final String menuPath = "Menu/";
}


