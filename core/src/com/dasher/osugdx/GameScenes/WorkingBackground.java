
package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;

public class WorkingBackground extends GameImage implements BeatmapManagerListener {
    private final Viewport viewport;
    private final TextureLoader.TextureParameter textureParameter;
    private boolean isFirstBGChange = true;
    public Texture defaultTexture;

    public WorkingBackground(@NotNull OsuGame game, Texture texture) {
        super(game, texture, false);
        defaultTexture = texture;
        textureParameter = game.assetManager.textures.textureParameter;
        viewport = game.viewport;
        setScaling(Scaling.fill);
        setAlign(Align.center);
        setOrigin(Align.center);
    }

    public void setBackground(@NotNull Texture texture) {
        TextureRegionDrawable drawable = (TextureRegionDrawable) getDrawable();
        Texture currentTexture = drawable.getRegion().getTexture();

        if (texture.equals(currentTexture)) {
            return;
        }
        if (!currentTexture.equals(defaultTexture)) {
            currentTexture.dispose();
        }

        float time = 1f / (isFirstBGChange? 4 : 8);

        if (isFirstBGChange) {
            isFirstBGChange = false;
            getColor().a = 0;
        }

        texture.setFilter(textureParameter.minFilter, textureParameter.magFilter);
        addAction(
                Actions.sequence(
                        Actions.fadeOut(time),
                        Actions.run(() -> drawable.getRegion().setTexture(texture)),
                        Actions.fadeIn(time)
                )
        );
    }

    @Override
    public void act(float delta) {
        setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        applyCentering(viewport);
        super.act(delta);
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
                setBackground(bgFile.exists()? new Texture(bgFile, true) : defaultTexture);
                break;
            }
        }
        if (!hasBackground) {
            setBackground(defaultTexture);
        }
    }

    @Override
    public void onNewBeatmapSet(BeatMapSet beatMapSet) {

    }
}
