package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.dasher.osugdx.Framework.Interfaces.UpdateAble;
import com.dasher.osugdx.OsuGame;
import com.rafaskoberg.gdx.parrot.Parrot;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AudioFactory implements UpdateAble {
    protected AsyncExecutor asyncExecutor;
    protected Parrot parrot;
    protected Array<GameMusic> musics = new Array<>();
    protected float musicVolume = 1;
    protected float soundVolume = 1;

    public AudioFactory(@NotNull OsuGame game) {
        this.asyncExecutor = game.asyncExecutor;
        this.parrot = game.parrot;
    }

    public GameMusic newMusic(Music music) {
        return new GameMusic(music, this);
    }
    
    public GameSound newSound(Sound sound) {
        return new GameSound(sound, this);
    }

    @Override
    public void update(float delta) {
        for (GameMusic music: musics) {
            music.update(delta);
        }
    }
}
