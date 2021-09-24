
package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureListener;
import com.dasher.osugdx.IO.Beatmaps.BeatMapSet;
import com.dasher.osugdx.IO.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.IO.Beatmaps.BeatmapUtils;
import com.dasher.osugdx.Images.GameImage;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.NotNull;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;

public class WorkingBackground extends GameImage implements BeatmapManagerListener, ReusableTextureListener {
    private final BeatmapUtils beatmapUtils;
    private final Viewport viewport;
    private boolean isFirstBGChange = true;
    private Texture nextTexture;
    public Texture defaultTexture;

    public WorkingBackground(@NotNull OsuGame game, Texture texture) {
        super(game, texture, false);
        defaultTexture = texture;
        viewport = game.viewport;
        beatmapUtils = game.beatmapUtils;
        setScaling(Scaling.fill);
        setAlign(Align.center);
        setOrigin(Align.center);
        if (game.beatmapManager.getCurrentMap() != null) {
            onNewBeatmap(game.beatmapManager.getCurrentMap());
        }
    }

    public void setBackground(@NotNull Texture texture) {
        nextTexture = texture;
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
        }

        addAction(
                Actions.sequence(
                        Actions.color(Color.CLEAR, time),
                        Actions.run(() -> drawable.getRegion().setTexture(texture)),
                        Actions.color(Color.WHITE, time)
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
                Texture backgroundTexture = beatmapUtils.getBackground(beatmap, this);
                hasBackground = backgroundTexture != null;
                setBackground(hasBackground? backgroundTexture : defaultTexture);
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

    @Override
    public boolean shouldDispose(Texture texture) {
        return nextTexture != texture;
    }
}
