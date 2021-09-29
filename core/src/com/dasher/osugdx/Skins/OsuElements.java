package com.dasher.osugdx.Skins;

public enum OsuElements {
    // Cursor
    CURSOR("cursor", Extensions.pngOnly),
    CURSOR_MIDDLE("cursormiddle", Extensions.pngOnly),
    CURSOR_TRAIL("cursortrail", Extensions.pngOnly),

    MENU_BUTTON_BG("menu-button-background", Extensions.pngOnly),

    // Stars
    STAR("star", Extensions.pngOnly),
    STAR2("star2",  Extensions.pngOnly),
    STAR3("star3", Extensions.pngOnly),

    // Song select
    MENU_BACK("menu-back", Extensions.pngOnly, true),

    // Select buttons
    SELECTION_MODS ("selection-mods", Extensions.pngOnly),
    SELECTION_MODS_OVERLAY ("selection-mods-over", Extensions.pngOnly),
    SELECTION_RANDOM ("selection-random", Extensions.pngOnly),
    SELECTION_RANDOM_OVERLAY ("selection-random-over", Extensions.pngOnly),
    SELECTION_OPTIONS ("selection-options", Extensions.pngOnly),
    SELECTION_OPTIONS_OVERLAY ("selection-options-over", Extensions.pngOnly),
    SELECTION_MODE("selection-mode", Extensions.pngOnly),
    SELECTION_MODE_OVERLAY("selection-mode-over", Extensions.pngOnly);

    private static class Extensions {
        public static final String png = "png";
        public static final String[] pngOnly = new String[]{png};
    }

    public final String name;
    public final String[] names;
    public final String[] extensions;
    public boolean isAnimated;

    OsuElements(String name, String[] extensions) {
        this.name = name;
        this.names = new String[]{name + "@2x", name};
        this.extensions = extensions;
        this.isAnimated = false;
    }

    OsuElements(String name, String[] extensions, boolean isAnimated) {
        this.name = name;
        this.names = new String[]{name+"-{i}@2x", name+"@2x", name};
        this.extensions = extensions;
        this.isAnimated = isAnimated;
    }
}
