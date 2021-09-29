package com.dasher.osugdx.GameScenes.Gameplay;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.Gameplay.Osu.OsuHitCircle;
import com.dasher.osugdx.GameScenes.SoundSelect.SoundSelectScreen;
import com.dasher.osugdx.Input.InputHelper;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuBeatmap;
import lt.ekgame.beatmap_analyzer.beatmap.osu.OsuObject;

public class GamePlayScreen extends GameScreen {
    private Beatmap gameplayBeatmap;
    private int currentComboObjectNumber = 0;
    private int amountComboSections = 0;
    private final ObjectMap<StatisticType, Float> statisticTimes = new ObjectMap<>();
    private final Stage hitObjectStage = new Stage(viewport);

    public GamePlayScreen(@NotNull OsuGame game, @NotNull Beatmap beatmap) {
        super(game);
        System.out.println("Gaming: " + beatmap.getMetadata().getTitleRomanized());
        gameplayBeatmap = beatmapUtils.createMap(beatmap);  // reInit objects
        beatmapManager.startMusicPlaying();
        if (gameplayBeatmap.getGenerals().getGamemode() == GameMode.OSU && gameplayBeatmap instanceof OsuBeatmap) {
            statisticTimes.put(StatisticType.AR, GameplayUtils.mapDifficultyRange((float) ((OsuBeatmap) gameplayBeatmap).getAR(), 1800, 1200, 450));
        }
        for (lt.ekgame.beatmap_analyzer.beatmap.HitObject hitObjectData: gameplayBeatmap.getHitObjects()) {
            GameObject<?> gameObject = null;
            if (hitObjectData instanceof OsuObject) {
                gameObject = new OsuHitCircle((OsuObject) hitObjectData, game, this);
            }
            hitObjectStage.addActor(gameObject);
        }
        inputMultiplexer.addProcessor(hitObjectStage);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

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

    public ObjectMap<StatisticType, Float> getStatisticTimes() {
        return statisticTimes;
    }

    public int getAmountComboSections() {
        return amountComboSections;
    }

    public int getCurrentComboObjectNumber() {
        return currentComboObjectNumber;
    }
}
