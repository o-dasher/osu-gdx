package com.dasher.osugdx.GameScenes.Gameplay.Osu;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.Framework.Actors.AnimatedSprite;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.GameScenes.Gameplay.GamePlayScreen;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public abstract class OsuCircleObject extends OsuGameObject {
    protected Image circle;
    protected Image approachCircle;
    protected AnimatedSprite circleOverlay;

    public OsuCircleObject(@NotNull OsuObject baseObject, OsuGame game, GamePlayScreen screen) {
        super(baseObject, game, screen);
        if (baseObject.isNewCombo()) {
            screen.resetCurrentComboObjectNumber();
        } else {
            screen.incrementCurrentComboObjectNumber();
        }
    }

    protected void init(@NotNull Image circle, @NotNull Image approachCircle, @NotNull AnimatedSprite circleOverlay) {
        this.circle = circle;
        this.approachCircle = approachCircle;
        this.circleOverlay = circleOverlay;
        circle.setOrigin(Align.center);
        approachCircle.setOrigin(Align.center);
        circleOverlay.setOrigin(Align.center);
        circleOverlay.setPosition(circleOverlay.getX() + circle.getWidth() / 2, circleOverlay.getY() + circle.getHeight() / 2);
        int overlayScale = 2;
        circleOverlay.setScale(overlayScale);
        addActor(circle);
        addActor(circleOverlay);
        addActor(approachCircle);
        initColor();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final float musicPosition = currentMusic.getPosition();
        float timeDiff = musicPosition - baseObject.getStartTimeS();
        float percentage = timeDiff / approachTime;
        float approachScale = 1 * (1 + 1 * (1 - percentage));
        approachCircle.setScale(approachScale);
        if (musicPosition >= baseObject.getStartTimeS()) {
            boolean isInTime = !(musicPosition >= baseObject.getStartTimeS() + approachTime);
            if (isInTime) {
                setVisible(true);
            } else {
                getParent().addAction(Actions.removeActor(this));
            }
        }
    }

    @Override
    public Color getColor(int comboSections) {
        Color color = null;
        ObjectMap<Integer, Color> colors = new ObjectMap<>();
        colors.put(1, Color.RED);
        colors.put(2, Color.GREEN);
        colors.put(3, Color.BLUE);
        colors.put(4, Color.YELLOW);
        for (int i = colors.size - 1; i >= 0; i--) {
            int c = i + 1;
            if (comboSections % c == 0) {
                color = colors.get(c);
                break;
            }
        }
        return color;
    }

    @Override
    public void applyToColor(Color color) {
        circle.setColor(color);
    }
}
