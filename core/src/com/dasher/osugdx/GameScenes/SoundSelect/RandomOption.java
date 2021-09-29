package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinElement;

import org.jetbrains.annotations.NotNull;

public class RandomOption extends FooterOption {
    public RandomOption(OsuGame game, SkinElement defaultE, SkinElement hoverE, @NotNull FooterOption previousOption, Stage menuOptionsStage) {
        super(game, defaultE, hoverE, previousOption, menuOptionsStage);
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.beatmapManager.randomizeCurrentBeatmapSet();
                return false;
            }
        });
    }
}
