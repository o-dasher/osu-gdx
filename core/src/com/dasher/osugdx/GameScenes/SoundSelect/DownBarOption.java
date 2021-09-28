package com.dasher.osugdx.GameScenes.SoundSelect;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.Skins.SkinElement;

import org.jetbrains.annotations.NotNull;

public class DownBarOption extends GameImage  {
    public Sprite defaultSprite;
    public Sprite hoverSprite;
    private final float scale = 0.5f;

    public DownBarOption(OsuGame game, @NotNull SkinElement defaultE, @NotNull SkinElement hoverE) {
        super(game, defaultE.getSprite(), false);
        this.defaultSprite = defaultE.getSprite();
        this.hoverSprite = hoverE.getSprite();
        setDrawable(new SpriteDrawable(defaultSprite));
        setScale(scale);
        SpriteDrawable drawable = ((SpriteDrawable) getDrawable());
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                drawable.setSprite(hoverSprite);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                drawable.setSprite(defaultSprite);
            }
        });
    }

    public DownBarOption(OsuGame game, SkinElement defaultE, SkinElement hoverE, @NotNull GameImage previousOption) {
        this(game, defaultE, hoverE);
        setPosition(previousOption.getX() + previousOption.getWidth() * scale, previousOption.getY());
    }
}
