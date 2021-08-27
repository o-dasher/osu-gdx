
package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;

public class WorkingBackground extends GameImage implements BeatmapManagerListener {
    private final Viewport viewport;
    private Texture currentTexture;
    private final TextureLoader.TextureParameter textureParameter;

    public Texture defaultTexture;

    public WorkingBackground(@NotNull OsuGame game, Texture texture) {
        super(game, texture, false);
        defaultTexture = texture;
        textureParameter = game.assetManager.textures.textureParameter;
        currentTexture = texture;
        viewport = game.viewport;
        setScaling(Scaling.fill);
        setAlign(Align.center);
        setOrigin(Align.center);
        setBackground(texture);
    }

    public void setBackground(Texture texture) {
        if (!currentTexture.equals(defaultTexture)) {
            currentTexture.dispose();
        }

        texture.setFilter(textureParameter.minFilter, textureParameter.magFilter);
        currentTexture = texture;
        float time = 1f / 8f;
        addAction(Actions.sequence(
                Actions.fadeOut(time),
                Actions.run(() -> setDrawable(new SpriteDrawable(new Sprite(currentTexture)))),
                Actions.fadeIn(time)
        ));
    }

    @Override
    public void act(float delta) {
        setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        applyCentering(viewport);
        super.act(delta);
    }

    public Texture getCurrentTexture() {
        return currentTexture;
    }


    @Override
    public void onNewBeatmap(@NotNull Beatmap beatmap) {
        boolean hasBackground = false;
        for (BreakPeriod breakPeriod: beatmap.getBreaks()) {
            if (breakPeriod.isBackground()) {
                hasBackground = true;
                FileHandle beatmapFile = Gdx.files.external(beatmap.beatmapFilePath);
                String bgFileName = breakPeriod.getBackgroundFileName();
                FileHandle bgFile = Gdx.files.external(beatmapFile.parent() + "/" + bgFileName);
                if (bgFile.exists()) {
                    setBackground(new Texture(bgFile, true));
                } else {
                    setBackground(defaultTexture);
                }
            }
        }
        if (!hasBackground) {
            setBackground(defaultTexture);
        }
    }
}
