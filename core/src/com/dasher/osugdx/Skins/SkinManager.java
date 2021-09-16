package com.dasher.osugdx.Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SkinManager {
    private final FileHandle defaultDir;
    private Skin selectedSkin;
    private final SkinConfigurator skinConfigurator;

    public SkinManager() {
        this.skinConfigurator = new SkinConfigurator();
        this.defaultDir = Gdx.files.local("Skins/Legacy");
        changeSkin(defaultDir);
    }

    public void changeSkin(@NotNull FileHandle fileHandle) {
        System.out.println("Changing skin: " + fileHandle.name());
        if (selectedSkin != null) {
            unloadElements();
        }
        selectedSkin = skinConfigurator.loadSkin(fileHandle);
        loadElements();
    }

    public void loadElements() {
        for (SkinElementNames elementName: SkinElementNames.values()) {
            for (String name: elementName.names) {
                for (String extension: elementName.extensions) {
                    String path = selectedSkin.getDirectory().path() + "/" +name+"."+ extension;
                    FileHandle file = Gdx.files.internal(path);
                    if (!file.exists()) {
                        continue;
                    }
                    ElementString string = new ElementString(file.nameWithoutExtension(), file.extension());
                    switch (elementName) {
                        case CURSOR:
                            selectedSkin.cursor = createElement(file, string);
                            break;
                        case CURSOR_MIDDLE: {
                            selectedSkin.cursorMiddle = createElement(file, string);
                            break;
                        }
                        case CURSOR_TRAIL: {
                            selectedSkin.cursorTrail = createElement(file, string);
                            break;
                        }
                        case MENU_BUTTON_BG: {
                            selectedSkin.menuButtonBG = createElement(file, string);
                        }
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void unloadElements() {
        for (SkinElement element: selectedSkin.elements) {
            element.getTexture().dispose();
            selectedSkin.elements.removeValue(element, true);
        }
    }

    @Contract("_, _ -> new")
    private @NotNull SkinElement defaultElement(FileHandle file, ElementString elementString) {
        file = Gdx.files.internal(defaultDir+"/"+file.nameWithoutExtension());
        return new SkinElement(file, elementString);
    }

    private SkinElement createElement(FileHandle file, ElementString elementString) {
        SkinElement skinElement;
        try {
            if (file.exists()) {
                if (elementString.getExtension().equals("png") || elementString.getExtension().equals("jpg")) {
                    skinElement = new SkinElement(file, elementString);
                }  else {
                    skinElement = defaultElement(file, elementString);
                }
            } else {
                skinElement = defaultElement(file, elementString);
            }
        } catch (Exception e) {
            skinElement = defaultElement(file, elementString);
        }
        selectedSkin.elements.add(skinElement);
        return skinElement;
    }

    public Skin getSelectedSkin() {
        return selectedSkin;
    }
}
