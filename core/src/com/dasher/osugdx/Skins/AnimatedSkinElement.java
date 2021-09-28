package com.dasher.osugdx.Skins;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;


public class AnimatedSkinElement {
    private final Skin skin;
    private Array<Sprite> sprites;
    private boolean isBeginSpriteInput;
    private Animation<Sprite> animation;

    public AnimatedSkinElement(Skin skin) {
        this.skin = skin;
    }

    public void setAnimation(Animation<Sprite> animation) {
        this.animation = animation;
    }

    public void beginSpriteInput() {
        isBeginSpriteInput = true;
        sprites = new Array<>();
    }

    public void addSprite(Sprite sprite) {
        if (isBeginSpriteInput) {
            sprites.add(sprite);
        } else {
            throw new IllegalStateException();
        }
    }

    public void endSpriteInput() {
        animation = new Animation<>(1f / sprites.size, sprites);
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public Animation<Sprite> getAnimation() {
        return animation;
    }
}
