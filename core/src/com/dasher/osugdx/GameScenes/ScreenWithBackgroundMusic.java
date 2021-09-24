package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;

import org.jetbrains.annotations.NotNull;

public interface ScreenWithBackgroundMusic extends Screen {
    default void playBackgroundMusic(@NotNull Music music) {
        music.setLooping(true);
        music.play();
    }
}
