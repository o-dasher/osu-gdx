package com.dasher.osugdx.GameScenes.Gameplay.Osu;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Actors.AnimatedSprite;
import com.dasher.osugdx.Framework.Interfaces.ResizeListener;
import com.dasher.osugdx.GameScenes.Gameplay.StatisticType;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public abstract class OsuCircleObject extends OsuGameObject implements ResizeListener {
    protected Image circle;
    protected Image approachCircle;
    protected AnimatedSprite circleOverlay;

    public OsuCircleObject(@NotNull OsuObject baseObject, OsuGame game, OsuPlayScreen screen) {
        super(baseObject, game, screen);
        if (baseObject.isNewCombo()) {
            screen.resetCurrentComboObjectNumber();
        } else {
            screen.incrementCurrentComboObjectNumber();
        }
    }

    @Override
    public void onResize() {
        if (getStage() == null) {
            return;
        }
        float width = getStage().getViewport().getWorldWidth();
        float height = getStage().getViewport().getWorldHeight();
        float swidth = width;
        float sheight = height;
        if (swidth * 3 > sheight * 4)
            swidth = sheight * 4 / 3;
        else
            sheight = swidth * 3 / 4;
        float xMultiplier = swidth / game.WORLD_WIDTH;
        float yMultiplier = sheight / game.WORLD_HEIGHT;
        int MAX_X = 512, MAX_Y = 384;
        int xOffset = (int) (width - MAX_X * xMultiplier) / 2;
        int yOffset = (int) (height - MAX_Y * yMultiplier) / 2;
        Vector2 position = baseObject.getPosition();
        setPosition(position.x * xMultiplier + xOffset, position.y * yMultiplier + yOffset);
    }

    public float getDiameter() {
        return 109 - 9 * (statistics.get(StatisticType.CS));
    }

    protected void init(@NotNull Image circle, @NotNull Image approachCircle, @NotNull AnimatedSprite circleOverlay) {
        this.circle = circle;
        this.approachCircle = approachCircle;
        this.circleOverlay = circleOverlay;
        circle.setOrigin(Align.center);
        circle.setAlign(Align.center);
        approachCircle.setOrigin(Align.center);
        approachCircle.setAlign(Align.center);
        circleOverlay.setOrigin(Align.center);
        circleOverlay.setPosition(circleOverlay.getX() + circle.getWidth() / 2, circleOverlay.getY() + circle.getHeight() / 2);
        circleOverlay.setScale(2);
        addActor(circle);
        addActor(circleOverlay);
        addActor(approachCircle);
        float diameter = getDiameter();
        setScale((diameter / circle.getWidth()) * 1.5f);
        initColor();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final float musicPosition = game.currentMusicPosition;
        float timeDiff = musicPosition - baseObject.getStartTimeS();
        float percentage = timeDiff / approachTime;
        float approachScale = 1 * (1 + 2 * (1 - percentage));
        approachCircle.setScale(approachScale);
    }

    @Override
    protected boolean finalizeCondition() {
        return game.currentMusicPosition >= baseObject.getStartTimeS() + approachTime;
    }

    @Override
    public Color getColor(int comboSections) {
        Color color = null;
        ObjectMap<Integer, Color> colors = new ObjectMap<>();
        Color[] comboColors = game.skinManager.getSelectedSkin().getComboColors();
        for (int i = 0; i < comboColors.length; i++) {
            colors.put(i + 1, comboColors[i]);
        }
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
