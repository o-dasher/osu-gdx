package com.dasher.osugdx.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.dasher.osugdx.assets.GameAssetManager;

import org.jetbrains.annotations.NotNull;

public class Fonts {
    public final BitmapFont baseBitmapFont;
    public final BitmapFont allerFont;

    public Fonts(@NotNull GameAssetManager assetManager) {
        baseBitmapFont = new BitmapFont(false);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal(
                        assetManager.assetPaths.fontsPath.path()
                                + assetManager.assetPaths.fontsPath.aller + "AllerDisplay.ttf"
                )
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.genMipMaps = true;
        allerFont = generator.generateFont(parameter);
        generator.dispose();
    }
}
