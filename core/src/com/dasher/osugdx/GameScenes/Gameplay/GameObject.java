package com.dasher.osugdx.GameScenes.Gameplay;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.HitObject;

public abstract class GameObject<T extends HitObject> extends Group {
    protected ObjectMap<StatisticType, Float> statistics;
    protected Music currentMusic;
    protected Skin skin;
    protected AbstractPlayScreen<?, ?> screen;
    protected OsuGame game;
    protected T baseObject;
    protected Color color;

    public GameObject(@NotNull T baseObject, @NotNull OsuGame game, @NotNull AbstractPlayScreen<?, ?> screen) {
        this.game = game;
        this.screen = screen;
        this.statistics = screen.getStatisticData();
        this.baseObject = baseObject;
        this.skin = game.skinManager.getSelectedSkin();
        currentMusic = game.beatmapManager.getCurrentMusic();
        setOrigin(Align.center);
        setVisible(false);
        color = getColor(screen.getAmountComboSections());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (finalizeCondition()) {
            Group parent = getParent();
            if (isVisible() && parent != null) {
                setVisible(false);
                parent.addAction(Actions.removeActor(this));
            }
        } else if (game.currentMusicPosition >= baseObject.getStartTimeS()) {
            if (!isVisible()) {
                setVisible(true);
            }
        }
    }

    protected abstract boolean finalizeCondition();

    protected void initColor() {
        applyToColor(color);
    }

    /*
    Use this to adjust this object color related to it being a new combo
     */
    protected abstract Color getColor(int comboSections);

    /*
    Apply the coloring here
     */
    protected abstract void applyToColor(Color color);

    public T getBaseObject() {
        return baseObject;
    }
}
