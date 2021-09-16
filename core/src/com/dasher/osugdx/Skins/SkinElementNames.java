package com.dasher.osugdx.Skins;

public enum SkinElementNames {
    CURSOR("cursor", new String[]{"png"}),
    CURSOR_MIDDLE ("cursormiddle", new String[]{"png"}),
    CURSOR_TRAIL ("cursortrail", new String[]{"png"}),
    MENU_BUTTON_BG ("menu-button-background", new String[]{"png"});

    public final String name;
    public final String[] names;
    public final String[] extensions;

    SkinElementNames(String name, String[] extensions) {
        this.name = name;
        this.names = new String[]{name, name + "@2x"};
        this.extensions = extensions;
    }
}
