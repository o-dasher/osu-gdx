package com.dasher.osugdx.Framework.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import org.jetbrains.annotations.NotNull;

public interface AudioHandler {
    default void playMusic(@NotNull Music music) {
        music.play();
    }
    default void playSound(@NotNull Sound sound) {
        sound.play();
    };
}
