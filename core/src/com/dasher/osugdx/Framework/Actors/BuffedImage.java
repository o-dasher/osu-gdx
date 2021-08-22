package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Audio.AudioHandler;
import com.dasher.osugdx.Framework.Graphics.GraphicHelper;

import org.jetbrains.annotations.NotNull;

public class BuffedImage extends Image {
    private boolean isEnteredExitScaledImage = false;

    public BuffedImage() {
        super();
    }

    public BuffedImage(Texture texture) {
        super(texture);
    }

    public void adjustSizeToFitOnScreen(@NotNull Viewport viewport) {
        setSize(viewport.getWorldWidth() / GraphicHelper.getAspectRatio(viewport), viewport.getWorldHeight());
    }

    protected void toEnterExitScaledImage() {
        float defaultScaleBy = 0.01f;
        float defaultScaleAnimationDuration = 0.1f;
        toEnterExitScaledImage(defaultScaleBy, defaultScaleAnimationDuration);
    }

    protected void toEnterExitScaledImage(float scaleBy, float scaleAnimationDuration) {
        isEnteredExitScaledImage = true;

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                event.getTarget().addAction(Actions.scaleBy(scaleBy, scaleBy, scaleAnimationDuration));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                event.getTarget().addAction(Actions.scaleBy(-scaleBy, -scaleBy, scaleAnimationDuration));
            }
        });
    }

    public void setOriginCenter() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public void toButton(Sound touchDownSound, AudioHandler audioHandler, Runnable onTouchDown) {
        if (!isEnteredExitScaledImage) {
            toEnterExitScaledImage();
        }
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                audioHandler.playSound(touchDownSound);
                onTouchDown.run();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void toButton(Sound touchDownSound, AudioHandler audioHandler) {
        toButton(touchDownSound, audioHandler, () -> {});
    }
}
