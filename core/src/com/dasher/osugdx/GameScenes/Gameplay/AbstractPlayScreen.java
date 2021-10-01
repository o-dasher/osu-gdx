package com.dasher.osugdx.GameScenes.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.Framework.Interfaces.ResizeListener;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.SoundSelect.SoundSelectScreen;
import com.dasher.osugdx.Input.InputHelper;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuBeatmap;

public abstract class AbstractPlayScreen<OBJECT_TYPE extends HitObject, BEATMAP_TYPE extends Beatmap> extends GameScreen {
    protected final BEATMAP_TYPE gameplayBeatmap;
    protected final Array<GameObject<OBJECT_TYPE>> gameObjects = new Array<>();
    private int currentComboObjectNumber = 0;
    private int amountComboSections = 0;
    private final ObjectMap<StatisticType, Float> statisticData = new ObjectMap<>();
    private final Stage hitObjectStage = new Stage(viewport);

    public void addGameObject(GameObject<OBJECT_TYPE> gameObject) {
        gameObjects.add(gameObject);
        hitObjectStage.addActor(gameObject);
        if (gameObject instanceof ResizeListener) {
            ((ResizeListener) gameObject).onResize();
        }
    }

    public AbstractPlayScreen(@NotNull OsuGame game, @NotNull BEATMAP_TYPE beatmap) {
        super(game);
        System.out.println("Gaming: " + beatmap.getMetadata().getTitleRomanized());
        gameplayBeatmap = getBeatmap(beatmap);
        beatmapManager.startMusicPlaying();
        putStatistics(statisticData);
        for (int i = gameplayBeatmap.getHitObjects().size - 1; i >= 0; i--) {
            HitObject hitObjectData = gameplayBeatmap.getHitObjects().get(i);
            GameObject<OBJECT_TYPE> gameObject = getObject(hitObjectData);
            if (gameObject == null) {
                System.out.println(hitObjectData.getClass().getName() + " not implemented for screen: " + getClass().getName());
            } else {
                addGameObject(getObject(hitObjectData));
            }
        }
        inputMultiplexer.addProcessor(hitObjectStage);
    }

    public abstract GameObject<OBJECT_TYPE> getObject(HitObject hitObjectData);
    public abstract BEATMAP_TYPE getBeatmap(Beatmap beatmap);
    public abstract void putStatistics(ObjectMap<StatisticType, Float> statisticTimes);

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        System.out.println(Gdx.graphics.getFramesPerSecond());

        renderBackground(delta);

        hitObjectStage.act(delta);
        hitObjectStage.draw();

        renderFade(delta);

        if (InputHelper.isBackPressed()) {
            if (!game.calledToSwitchScreen) {
                switchScreen(new SoundSelectScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void resetCurrentComboObjectNumber() {
        this.currentComboObjectNumber = 0;
        amountComboSections++;
    }

    public void incrementCurrentComboObjectNumber() {
        currentComboObjectNumber++;
    }

    public ObjectMap<StatisticType, Float> getStatisticData() {
        return statisticData;
    }

    public int getAmountComboSections() {
        return amountComboSections;
    }

    public int getCurrentComboObjectNumber() {
        return currentComboObjectNumber;
    }
}
