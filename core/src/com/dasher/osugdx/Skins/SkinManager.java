package com.dasher.osugdx.Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.dasher.osugdx.OsuGame;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
        for (OsuElements elementName: OsuElements.values()) {
            SkinElement createdElement;
            boolean isBreak = false;
            for (String name: elementName.names) {
                for (String extension: elementName.extensions) {
                    String base = selectedSkin.getDirectory().path() + "/";
                    String sub = base+name;
                    AnimatedSkinElement animatedSkinElement = null;
                    if (elementName.isAnimated) {
                        animatedSkinElement = new AnimatedSkinElement(selectedSkin);
                        if (name.contains("{i}")) {
                            animatedSkinElement.beginSpriteInput();
                            int i = -1;
                            while (true) {
                                i++;
                                String indexedSub = base + name.replace("{i}", String.valueOf(i));
                                String currentPath = indexedSub + "." + extension;
                                FileHandle file = Gdx.files.internal(currentPath);
                                if (!file.exists()) {
                                    break;
                                }
                                SkinElement element = getElement(file);
                                if (element == null) {
                                    break;
                                } else {
                                    animatedSkinElement.addSprite((element.getSprite()));
                                }
                            }
                            animatedSkinElement.endSpriteInput();
                            if (animatedSkinElement.getSprites().size > 0) {
                                selectedSkin.animatedElements.put(elementName, animatedSkinElement);
                                isBreak = true;
                                break;
                            }
                        }
                    }
                    String path = sub+"."+extension;
                    FileHandle file = Gdx.files.internal(path);
                    createdElement = getElement(file);
                    if (createdElement == null) {
                        continue;
                    }
                    if (animatedSkinElement != null) {
                        animatedSkinElement.beginSpriteInput();
                        animatedSkinElement.addSprite(createdElement.getSprite());
                        animatedSkinElement.endSpriteInput();
                    }
                    System.out.println(" element loaded: "+createdElement.getFile().path());
                    if (elementName.isAnimated) {
                        selectedSkin.animatedElements.put(elementName, animatedSkinElement);
                    } else {
                        selectedSkin.elements.put(elementName, createdElement);
                    }
                    isBreak = true;
                    break;
                }
                if (isBreak) {
                    break;
                }
            }
        }
    }

    public SkinElement getElement(@NotNull FileHandle file) {
        SkinElement skinElement = null;
        ElementString string = new ElementString(file.nameWithoutExtension(), file.extension());
        try {
            skinElement = createElement(file, string);
        } catch (Exception ignore) { }
        return skinElement;
    }

    private void unloadElements() {
        for (ObjectMap.Entry<OsuElements, SkinElement> entry: selectedSkin.elements) {
            entry.value.dispose();
            selectedSkin.elements.remove(entry.key);
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
