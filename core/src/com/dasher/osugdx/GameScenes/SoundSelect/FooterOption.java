package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FooterOption extends Actor {
    private final GameImage defaultImage;
    private GameImage hoverImage;
    private final int w;
    private final int h = 90;
    private final float scale = 0.5f;

    public FooterOption(OsuGame game, @NotNull SkinElement defaultE, @NotNull SkinElement hoverE, @NotNull Stage menuOptionStage, int w) {
        this.w = w;
        this.defaultImage = new GameImage(game, defaultE.getSprite(), false);
        this.hoverImage = new GameImage(game, hoverE.getSprite(), false);
        menuOptionStage.addActor(defaultImage);
        menuOptionStage.addActor(hoverImage);
        menuOptionStage.addActor(this);
        setScale(scale);
        setOrigin(Align.bottomLeft);
        hoverImage.getColor().a = 0;
        hoverImage.setTouchable(Touchable.disabled);
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverImage.getColor().a = 1;
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoverImage.getColor().a = 0;
            }
        });
    }

    public FooterOption(OsuGame game, @NotNull SkinElement defaultE, @NotNull Stage menuOptionsStage, int w) {
        this.w = w;
        this.defaultImage = new GameImage(game, defaultE.getSprite(), false);
        menuOptionsStage.addActor(defaultImage);
        setScale(scale);
    }



    public FooterOption(OsuGame game, SkinElement defaultE, SkinElement hoverE, @NotNull FooterOption previousOption, Stage menuOptionsStage, int w) {
        this(game, defaultE, hoverE, menuOptionsStage, w);
        setPosition(previousOption.getX() + previousOption.w, 0);
    }

    public FooterOption(OsuGame game, SkinElement defaultE, SkinElement hoverE, @NotNull FooterOption previousOption, Stage menuOptionsStage) {
        this(game, defaultE, hoverE,  previousOption, menuOptionsStage, 77);
    }

    @Override
    public void setPosition(float x, float y) {
        defaultImage.setPosition(x, y);
        if (hoverImage != null) {
            hoverImage.setPosition(x, y);
        }
        setBounds(x, y, w, h);
    }

    @Override
    public float getWidth() {
        return defaultImage.getWidth();
    }

    @Override
    public float getX() {
        return defaultImage.getX();
    }

    @Override
    public float getY() {
        return defaultImage.getY();
    }

    @Override
    public void setScale(float scaleXY) {
        defaultImage.setScale(scaleXY);
        if (hoverImage != null) {
            hoverImage.setScale(scaleXY);
        }
    }

    public GameImage getDefaultImage() {
        return defaultImage;
    }

    public @Nullable GameImage getHoverImage() {
        return hoverImage;
    }
}
