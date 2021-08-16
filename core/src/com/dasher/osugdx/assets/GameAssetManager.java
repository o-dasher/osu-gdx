package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameAssetManager extends AssetManager {
    private final List<AssetHolder<?>> holders = new ArrayList<>();

    public final TextureHolder textures;
    public final SoundsHolder sounds;
    public final MusicHolder musics;
    public final TextureAtlasHolder textureAtlases;
    public final FontHolder fonts;

    public GameAssetManager() {
        com.dasher.osugdx.assets.AssetPaths assetPaths = new AssetPaths();
        holders.add(textures = new TextureHolder(assetPaths));
        holders.add(sounds = new SoundsHolder(assetPaths));
        holders.add(musics = new MusicHolder(assetPaths));
        holders.add(fonts = new FontHolder(assetPaths));
        holders.add(textureAtlases = new TextureAtlasHolder(assetPaths));
    }

    public void load() {
        for (com.dasher.osugdx.assets.AssetHolder<?> assetHolder: holders) {
            for (AssetDescriptor<?> assetDescriptor: assetHolder.assets) {
                load(assetDescriptor);
                System.out.println("Loaded asset: " + assetDescriptor.fileName);
            }
        }
    }
}
