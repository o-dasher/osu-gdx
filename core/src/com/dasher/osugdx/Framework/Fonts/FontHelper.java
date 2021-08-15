package com.dasher.osugdx.Framework.Fonts;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.jetbrains.annotations.NotNull;

public class FontHelper {
    public static void drawFontInCenter(Batch batch, @NotNull GlyphLayout glyphLayout, String text, BitmapFont font, @NotNull Viewport viewport) {
        glyphLayout.setText(font, text);
        float w = glyphLayout.width;
        font.draw(batch, glyphLayout, (viewport.getWorldWidth() - w) / 2, viewport.getWorldHeight() / 2);
    }
}
