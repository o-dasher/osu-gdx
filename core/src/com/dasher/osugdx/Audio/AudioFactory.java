package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioFactory {
    public GameMusic newMusic(Music music) {
        return new GameMusic(music);
    }
    
    public GameSound newSound(Sound sound) {
        return new GameSound(sound);
    }
}
