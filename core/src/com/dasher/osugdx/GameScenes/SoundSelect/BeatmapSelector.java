package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Tasks.ClockTask;
import com.dasher.osugdx.GameScenes.GameScreen;
import com.dasher.osugdx.GameScenes.Gameplay.GamePlayScreen;
import com.dasher.osugdx.Skins.OsuElements;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManager;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.Skin;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerReferencesListener;
import com.dasher.osugdx.osu.Mods.ModManagerListener;

import org.jetbrains.annotations.NotNull;


import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;

public
class BeatmapSelector extends Selector implements BeatmapManagerListener, ModManagerListener, BeatmapManagerReferencesListener {
    protected Beatmap beatmap;
    protected BeatMapSet beatmapSet;
    protected final Array<GameImage> stars = new Array<>();
    protected final Array<ClockTask> generateStarsTasks = new Array<>();
    private final Label diffLabel;
    private final Color inactiveColor;
    private final BeatmapSetSelector beatmapSetSelector;
    private boolean generatedStars = false;
    protected ClickListener toGameplayListener;

    public BeatmapSelector(
            OsuGame game, @NotNull Skin skin, BeatmapManager beatmapManager,
            SoundSelectScreen soundSelectScreen,
            BitmapFont font, Label.LabelStyle labelStyle,
            BeatMapSet beatmapSet, @NotNull Beatmap beatmap, Color inactiveColor, boolean allowThumbnails, BeatmapSetSelector beatmapSetSelector
    ) {
        super(game, skin, beatmapManager, soundSelectScreen, font, labelStyle, allowThumbnails);
        this.beatmap = beatmap;
        this.beatmapSetSelector = beatmapSetSelector;
        this.beatmapSet = beatmapSet;
        this.inactiveColor = inactiveColor;
        initLabels();
        diffLabel = createLabel(metadata().getVersion());
        diffLabel.setPosition(middleLabel.getX(), middleLabel.getY() - middleLabel.getHeight() * labelScale);
        adjustColor();
        generateStars();
    }

    protected void generateStars() {
        if (generatedStars) {
            return;
        }
        generatedStars = true;
        float starX = diffLabel.getX();
        // 10 - 1 to account for last star which can be floated
        float maxStars = Math.min(10 - 1, (int) beatmap.getBaseStars());
        float starScale = 0.25f;
        for (int i = 0; i < maxStars + 1; i++) {
            Sprite starSprite = skin.elements.get(OsuElements.STAR).getSprite();
            if (i == maxStars) {
                // Last star can be "float"
                float floatingStar = (float) (beatmap.getBaseStars() - i);
                TextureRegion region = new TextureRegion(
                        starSprite.getTexture(),
                        0, 0,
                        (int) (starSprite.getWidth() * floatingStar * 2), (int) (starSprite.getHeight() * 2)
                );
                starSprite = new Sprite(region);
            }
            GameImage star = new GameImage(game, starSprite, false);
            star.setPosition(
                    starX,
                    diffLabel.getY() - star.getHeight() * starScale * labelScale
            );
            star.setOrigin(Align.bottomLeft);
            star.setTouchable(Touchable.disabled);
            starX += star.getWidth() * starScale;
            stars.add(star);
            addActor(star);
        }
    }

    protected void animateStars() {
        float starScale = 0.25f;
        for (int i = 0; i < stars.size; i++) {
            GameImage star = stars.get(i);
            star.getColor().a = 0;
            final float clockTime = (i + 1) * 0.1f;
            generateStarsTasks.add(new ClockTask(clockTime) {
                @Override
                public void run() {
                    star.setScale(0);
                    star.getColor().a = 0;
                    star.addAction(
                            Actions.parallel(
                                    Actions.fadeIn(clockTime),
                                    Actions.scaleTo(starScale, starScale, clockTime)
                            )
                    );
                }
            });
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (ClockTask task: generateStarsTasks) {
            task.update(delta);
            if (task.isCancelled()) {
                generateStarsTasks.removeValue(task, true);
            }
        }
    }

    @Override
    public BeatmapMetadata metadata() {
        return beatmap.getMetadata();
    }

    @Override
    public boolean mapChangeCondition() {
        return beatmapManager.getCurrentMap() != beatmap && beatmapManager.getCurrentBeatmapSet() == beatmapSet;
    }

    @Override
    public void changeMap() {
        if (beatmapManager.getCurrentBeatmapSet() == beatmapSet) {
            beatmapManager.setCurrentMap(beatmap);
        }
    }

    @Override
    public void adjustColor() {
        super.adjustColor();
        if (isThisMapSelected()) {
            for (Label label: labels) {
                label.addAction(Actions.color(skin.getSongSelectActiveTextColor()));
            }
        }
    }

    @Override
    public Color activeColor() {
        return Color.WHITE;
    }

    @Override
    public Color inactiveColor() {
        return inactiveColor;
    }

    @Override
    public void disableSelector(Selector selector) {
        if (selector != null) {
            super.disableSelector(selector);
            if (selector instanceof BeatmapSelector) {
                BeatmapSelector beatmapSelector = ((BeatmapSelector) selector);
                ClickListener gameplayListener = beatmapSelector.toGameplayListener;
                if (gameplayListener != null) {
                    beatmapSelector.removeListener(gameplayListener);
                    beatmapSelector.toGameplayListener = null;
                }
            }
            for (Label label: selector.labels) {
                label.addAction(Actions.color(skin.getSongSelectInactiveTextColor(), colorSelectTime));
            }
        }
    }

    @Override
    public boolean isThisMapSelected() {
        return beatmapManager.getCurrentMap().beatmapFilePath.equals(beatmap.beatmapFilePath);
    }

    @Override
    public void changeSelectedSelector() {
        disableSelector(soundSelectScreen.selectedBeatmap);
        soundSelectScreen.selectedBeatmap = this;
        addListener(toGameplayListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!game.calledToSwitchScreen) {
                    GameScreen gameScreen = (GameScreen) game.getScreen();
                    gameScreen.switchScreen(new GamePlayScreen(game, beatmap));
                }
                return false;
            }
        });
    }

    @Override
    public Selector getSelectedSelector() {
        return soundSelectScreen.selectedBeatmap;
    }

    @Override
    public void onBeatmapCalculated(Beatmap beatmap) {
        if (beatmap.beatmapFilePath.equals(this.beatmap.beatmapFilePath)) {
            generateStars();
            if (beatmapSetSelector.isThisMapSelected()) {
                animateStars();
            }
        }
    }

    @Override
    public void onCompleteCalculation(Array<BeatMapSet> calculatedBeatmapSets) {

    }

    @Override
    public Beatmap getBeatmapReference() {
        return this.beatmap;
    }

    @Override
    public void setBeatmapReference(Beatmap beatmap) {
        this.beatmap = beatmap;
    }

    @Override
    public void dispose() {

    }
}
