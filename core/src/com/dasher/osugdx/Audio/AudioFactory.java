package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class AudioFactory {
    AsyncExecutor asyncExecutor;

    public AudioFactory(@NotNull OsuGame game) {
        asyncExecutor = game.asyncExecutor;
    }

    public GameMusic newMusic(Music music) {
        return new GameMusic(music);
    }
    
    public GameSound newSound(Sound sound) {
        return new GameSound(sound);
    }
}
