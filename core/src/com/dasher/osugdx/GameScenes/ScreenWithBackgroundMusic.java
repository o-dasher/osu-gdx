package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.audio.Music;
import com.dasher.osugdx.Audio.AudioManager;

import org.jetbrains.annotations.NotNull;

public interface ScreenWithBackgroundMusic {
    default void playBackgroundMusic(@NotNull AudioManager audioManager, @NotNull Music music) {
        music.setLooping(true);
        audioManager.playMusic(music);
    }
}
