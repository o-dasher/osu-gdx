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
            SkinElement createdElement = null;
            for (String name: elementName.names) {
                for (String extension: elementName.extensions) {
                    String path = selectedSkin.getDirectory().path() + "/" +name+"."+ extension;
                    FileHandle file = Gdx.files.internal(path);
                    ElementString string = new ElementString(file.nameWithoutExtension(), file.extension());
                    try {
                        createdElement = createElement(file, string);
                    } catch (Exception e) {
                        continue;
                    }
                    switch (elementName) {
                        case CURSOR:
                            selectedSkin.cursor = createdElement;
                            break;
                        case CURSOR_MIDDLE: {
                            selectedSkin.cursorMiddle = createdElement;
                            break;
                        }
                        case CURSOR_TRAIL: {
                            selectedSkin.cursorTrail = createdElement;
                            break;
                        }
                        case MENU_BUTTON_BG: {
                            selectedSkin.menuButtonBG = createdElement;
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            selectedSkin.elements.add(createdElement);
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
        return new SkinElement(file, elementString, selectedSkin);
    }

    private SkinElement createElement(FileHandle file, ElementString elementString) {
        SkinElement skinElement;
        try {
            if (file.exists()) {
                if (elementString.getExtension().equals("png") || elementString.getExtension().equals("jpg")) {
                    skinElement = new SkinElement(file, elementString, selectedSkin);
                }  else {
                    skinElement = defaultElement(file, elementString);
                }
            } else {
                skinElement = defaultElement(file, elementString);
            }
        } catch (Exception e) {
            skinElement = defaultElement(file, elementString);
        }
        return skinElement;
    }

    public Skin getSelectedSkin() {
        return selectedSkin;
    }
}
