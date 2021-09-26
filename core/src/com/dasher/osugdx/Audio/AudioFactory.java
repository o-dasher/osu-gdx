package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.dasher.osugdx.OsuGame;
import com.rafaskoberg.gdx.parrot.Parrot;

import org.jetbrains.annotations.NotNull;

public class AudioFactory {
    private final Parrot parrot;
    protected float musicVolume = 1;
    protected float soundVolume = 1;

    public AudioFactory(Parrot parrot) {
        this.parrot = parrot;
    }

    public GameMusic newMusic(Music music) {
        return new GameMusic(music, this);
    }
    
    public GameSound newSound(Sound sound) {
        return new GameSound(sound, this);
    }
}
