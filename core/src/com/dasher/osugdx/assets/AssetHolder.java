package com.dasher.osugdx.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class AssetHolder <T> {
    private final String basePath;

    public AssetHolder(String folderPath) {
        basePath = folderPath;
    }

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract(pure = true)
    protected String newPathString(String string) {
        return basePath + string;
    }

    protected List<AssetDescriptor<T>> assets = new ArrayList<>();

    public AssetDescriptor<T> addAsset(String path, Class<T> tClass) {
        AssetDescriptor<T> tAssetDescriptor = new AssetDescriptor<>(newPathString(path), tClass);
        assets.add(tAssetDescriptor);
        return tAssetDescriptor;
    }

    public AssetDescriptor<T> addAsset(String path, Class<T> tClass, AssetLoaderParameters<T> assetLoaderParameters) {
        AssetDescriptor<T> tAssetDescriptor = new AssetDescriptor<>(newPathString(path), tClass, assetLoaderParameters);
        assets.add(tAssetDescriptor);
        return tAssetDescriptor;
    }
}
