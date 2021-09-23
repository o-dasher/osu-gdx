package com.dasher.osugdx.Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SkinManager {
    private final OsuGame game;
    private final FileHandle defaultDir;
    private Skin selectedSkin;
    private final SkinConfigurator skinConfigurator;

    public SkinManager(OsuGame game) {
        this.game = game;
        this.skinConfigurator = new SkinConfigurator();
        this.defaultDir = Gdx.files.local("Skins/Legacy");
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
                        case STAR:
                            selectedSkin.star1 = createdElement;
                            break;
                        case STAR2:
                            selectedSkin.star2 = createdElement;
                            break;
                        case STAR3:
                            selectedSkin.star3 = createdElement;
                            break;
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
            element.dispose();
            selectedSkin.elements.removeValue(element, true);
        }
    }

    @Contract("_, _ -> new")
    private @NotNull SkinElement defaultElement(FileHandle file, ElementString elementString) {
        file = Gdx.files.internal(defaultDir+"/"+file.nameWithoutExtension());
        return new SkinElement(file, elementString, selectedSkin, game.assetManager.textures.textureParameter);
    }

    private SkinElement createElement(FileHandle file, ElementString elementString) {
        SkinElement skinElement;
        try {
            if (file.exists()) {
                if (elementString.getExtension().equals("png") || elementString.getExtension().equals("jpg")) {
                    skinElement = new SkinElement(file, elementString, selectedSkin, game.assetManager.textures.textureParameter);
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

    public FileHandle getDefaultDir() {
        return defaultDir;
    }
}
