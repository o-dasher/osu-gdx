package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

public class AudioFactory {
    protected float musicVolume = 1;
    protected float soundVolume = 1;

    public GameMusic newMusic(Music music) {
        return new GameMusic(music, this);
    }
    
    public GameSound newSound(Sound sound) {
        return new GameSound(sound, this);
    }
}
