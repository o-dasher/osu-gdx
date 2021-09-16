package com.dasher.osugdx.Skins;

import org.jetbrains.annotations.NotNull;

public class ElementString {
    private boolean isHD = false;
    private String string = "";
    private final String extension;

    public ElementString(@NotNull String string, String extension) {
        this.extension = extension;
        String HD_SUFFIX = "@2x";
        if (string.endsWith(HD_SUFFIX)) {
            isHD = true;
            string = string.substring(0, string.length() - HD_SUFFIX.length());
        }
        this.string = string;
    }

    public String getString() {
        return string;
    }
    public boolean isHD() {
        return isHD;
    }

    public String getExtension() {
        return extension;
    }
}
