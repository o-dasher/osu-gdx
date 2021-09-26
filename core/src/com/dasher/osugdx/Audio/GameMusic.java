package com.dasher.osugdx.Audio;

import com.badlogic.gdx.audio.Music;
import com.dasher.osugdx.Framework.Interfaces.UpdateAble;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.rafaskoberg.gdx.parrot.music.ParrotMusicType;


import org.jetbrains.annotations.NotNull;


public class GameMusic implements ParrotMusicType, Music, UpdateAble {
    private final AudioFactory audioFactory;
    private final Music music;
    private final int channel;
    private float setPosition;
    private boolean asynchronous;
    private boolean didSetPosition;
    private boolean isDisposed = false;
    private ClockTask disposeTask;


    public GameMusic(@NotNull Music music, @NotNull AudioFactory audioFactory) {
        this.music = music;
        this.audioFactory = audioFactory;
        this.channel = audioFactory.musics.size + 1;
        audioFactory.musics.add(this);
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public void playParrot() {
        audioFactory.parrot.playMusic(this, false, false, channel, channel);
    }

    public void playParrot(boolean loop, boolean fadeIn) {
        audioFactory.parrot.playMusic(this, loop, fadeIn, channel, channel);
    }

    public void playParrot(boolean loop) {
       audioFactory.parrot.playMusic(this, loop, false, channel, channel);
    }

    @Override
    public void play() {
        // AUDIO PLAYING ITSELF MUST BE DONE ON THE GL THREAD OTHERWISE SOME DEVICES MAY CRASH,
        // NOT MUCH OF A PROBLEM SINCE THIS IS DONE ASYNCHRONOUSLY ANYWAYS,
        // THE ASYNCHRONOUS BOOLEAN IS JUST FOR THE SET POSITION NOT STOP THE MAIN THREAD
        if (!isDisposed) {  // disposal check for asynchronous.
            // TRY CATCH BECAUSE THE MUSIC MAY BE DISPOSED LATER ANYWAYS OR THE BUFFER WASN'T LOADED
            try {
                music.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void stop() {
        setPosition = 0;
        didSetPosition = false;
        music.stop();
    }

    @Override
    public boolean isPlaying() {
        return !isDisposed && music.isPlaying();
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
        music.setVolume(volume);
    }

    @Override
    public float getVolume() {
        return music.getVolume();
    }

    @Override
    public void setPan(float pan, float volume) {
        music.setPan(pan, volume);
    }

    public void forcePosition(float position) {
        setPosition = position;
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
        audioFactory.parrot.stopMusic(this,true);
        GameMusic thisMusic = this;
        disposeTask = new ClockTask(audioFactory.parrot.getSettings().musicFadeOutDuration) {
            @Override
            public void run() {
                music.dispose();
                audioFactory.musics.removeValue(thisMusic, true);
                isDisposed = true;
                System.out.println("GameMusic: Disposed music at channel: " + channel);
            }
        };
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

    @Override
    public int hashCode() {
        return music.hashCode();
    }

    @Override
    public Music getMusic() {
        return this;
    }

    @Override
    public float getRelativeVolume() {
        return audioFactory.musicVolume;
    }

    @Override
    public void update(float delta) {
        if (!isDisposed && disposeTask != null) {
            disposeTask.update(delta);
        }
        if (asynchronous) {
            if (!didSetPosition && setPosition > 0) {
                if (audioFactory.parrot.isMusicPlaying(channel)) {
                    System.out.println("Parrot finished waiting for channel: " + channel);
                    audioFactory.asyncExecutor.submit(() -> {
                        setPosition(setPosition);
                        return null;
                    });
                    didSetPosition = true;
                } else {
                    System.out.println("Parrot is waiting for: " + channel);
                }
            }
        }
    }
}
