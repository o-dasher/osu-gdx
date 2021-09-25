package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;


import org.jetbrains.annotations.NotNull;

class GameMusic implements Music {
    private final AudioFactory audioFactory;
    private final Music music;
    private float baseVolume = 1;

    public GameMusic(@NotNull Music music, @NotNull AudioFactory audioFactory) {
        this.music = music;
        this.audioFactory = audioFactory;
        setVolume(audioFactory.musicVolume);
    }

    @Override
    public void play() {
        music.setVolume(audioFactory.musicVolume * baseVolume);
        music.play();
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void stop() {
        music.stop();
    }

    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    @Override
    public void setLooping(boolean isLooping) {
        music.setLooping(isLooping);
    }

    @Override
    public boolean isLooping() {
        return music.isLooping();
    }

    @Override
    public void setVolume(float volume) {
        this.baseVolume = volume;
    }

    @Override
    public float getVolume() {
        return music.getVolume();
    }

    @Override
    public void setPan(float pan, float volume) {
        music.setPan(pan, volume);
    }

    @Override
    public void setPosition(float position) {
        music.setPosition(position);
    }

    @Override
    public float getPosition() {
        return music.getPosition();
    }

    @Override
    public void dispose() {
        music.dispose();
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

    @Override
    public int hashCode() {
        return music.hashCode();
    }
}
