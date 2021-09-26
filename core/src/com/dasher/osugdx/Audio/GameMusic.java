package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.rafaskoberg.gdx.parrot.music.ParrotMusicType;


import org.jetbrains.annotations.NotNull;

public class GameMusic implements ParrotMusicType, Music {
    private final AudioFactory audioFactory;
    private final Music music;

    public GameMusic(@NotNull Music music, @NotNull AudioFactory audioFactory) {
        this.music = music;
        this.audioFactory = audioFactory;
    }

    @Override
    public int hashCode() {
        return music.hashCode();
    }

    @Override
    public Music getMusic() {
        return music;
    }

    @Override
    public float getRelativeVolume() {
        return audioFactory.musicVolume;
    }
}
