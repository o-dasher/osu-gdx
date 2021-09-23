package com.dasher.osugdx.Skins;

public enum SkinElementNames {
    CURSOR("cursor", Extensions.pngOnly),
    CURSOR_MIDDLE("cursormiddle", Extensions.pngOnly),
    CURSOR_TRAIL("cursortrail", Extensions.pngOnly),
    MENU_BUTTON_BG("menu-button-background", Extensions.pngOnly),
    STAR("star", Extensions.pngOnly),
    STAR2("star2",  Extensions.pngOnly),
    STAR3("star3", Extensions.pngOnly);

    private static class Extensions {
        public static final String png = "png";
        public static final String[] pngOnly = new String[]{png};
    }

    public final String name;
    public final String[] names;
    public final String[] extensions;

    SkinElementNames(String name, String[] extensions) {
        this.name = name;
        this.names = new String[]{name, name + "@2x"};
        this.extensions = extensions;
    }
}
