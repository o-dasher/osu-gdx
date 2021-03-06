
package com.dasher.osugdx.GameScenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.crashinvaders.vfx.effects.VfxEffect;
import com.dasher.osugdx.Framework.Graphics.Textures.ReusableTextureListener;
import com.dasher.osugdx.osu.Beatmaps.BeatMapSet;
import com.dasher.osugdx.osu.Beatmaps.BeatmapManagerListener;
import com.dasher.osugdx.osu.Beatmaps.BeatmapUtils;
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
            getColor().a = 0;
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

        float time = isFirstBGChange? 0.25f :  0.2f;

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
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
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
    public void onPreBeatmapChange() {

    }

    @Override
    public boolean shouldDispose(Texture texture) {
        return nextTexture != texture;
    }
}
