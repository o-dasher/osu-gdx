package com.dasher.osugdx.Framework.Interfaces;

public interface ResizeListener {
    default void onResize() {}
    default void onResize(int width, int height) {}
}
