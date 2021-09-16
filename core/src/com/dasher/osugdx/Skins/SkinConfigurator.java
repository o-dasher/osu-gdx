package com.dasher.osugdx.Skins;



import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Utils.BaseUtils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Loads skin configuration files.
 */
public class SkinConfigurator {
    /**
     * Returns a list of all subdirectories in the Skins directory.
     * @param root the root directory (search has depth 1)
     * @return an array of skin directories
     */
    public FileHandle[] getSkinDirectories(@NotNull FileHandle root) {
        Array<FileHandle> dirs = new Array<>();
        for (FileHandle dir : root.list(File::isDirectory)) {
            dirs.add(dir);
        }
        return dirs.toArray();
    }

    /**
     * Loads a skin configuration file.
     * If 'skin.ini' is not found, or if any fields are not specified, the
     * default values will be used.
     * @param dir the skin directory
     * @return the loaded skin
     */
    public Skin loadSkin(FileHandle dir) {
        String CONFIG_FILENAME = "skin.ini";
        FileHandle skinFile = Gdx.files.getFileHandle(dir + "/" + CONFIG_FILENAME, dir.type());
        Skin skin = new Skin(dir);
        if (!skinFile.exists() || skinFile.isDirectory())
            return skin;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(skinFile.read()));
        } catch (Exception ignore) {
            System.out.println((String.format("Failed to read file '%s'.", skinFile.path())));
        }

        if (in == null) {
            return skin;
        }

        try {
            String line = in.readLine();
            String[] tokens;
            while (line != null) {
                line = line.trim();
                if (isLineInvalid(line)) {
                    line = in.readLine();
                    continue;
                }
                switch (line) {
                    case "[General]":
                        while ((line = in.readLine()) != null) {
                            line = line.trim();
                            if (isLineInvalid(line))
                                continue;
                            if (line.charAt(0) == '[')
                                break;
                            if ((tokens = tokenize(line)) == null)
                                continue;
                            try {
                                switch (tokens[0]) {
                                    case "Name":
                                        skin.name = tokens[1];
                                        break;
                                    case "Author":
                                        skin.author = tokens[1];
                                        break;
                                    case "Version":
                                        if (tokens[1].equalsIgnoreCase("latest"))
                                            skin.version = Skin.LATEST_VERSION;
                                        else
                                            skin.version = Float.parseFloat(tokens[1]);
                                        break;
                                    case "SliderBallFlip":
                                        skin.sliderBallFlip = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "CursorRotate":
                                        skin.cursorRotate = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "CursorExpand":
                                        skin.cursorExpand = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "CursorCentre":
                                        skin.cursorCentre = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "SliderBallFrames":
                                        skin.sliderBallFrames = Integer.parseInt(tokens[1]);
                                        break;
                                    case "HitCircleOverlayAboveNumber":
                                        skin.hitCircleOverlayAboveNumber = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "spinnerFrequencyModulate":
                                        skin.spinnerFrequencyModulate = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "LayeredHitSounds":
                                        skin.layeredHitSounds = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "SpinnerFadePlayfield":
                                        skin.spinnerFadePlayfield = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "SpinnerNoBlink":
                                        skin.spinnerNoBlink = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "AllowSliderBallTint":
                                        skin.allowSliderBallTint = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "AnimationFramerate":
                                        skin.animationFramerate = Integer.parseInt(tokens[1]);
                                        break;
                                    case "CursorTrailRotate":
                                        skin.cursorTrailRotate = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "CustomComboBurstSounds":
                                        String[] split = tokens[1].split(",");
                                        int[] customComboBurstSounds = new int[split.length];
                                        for (int i = 0; i < split.length; i++)
                                            customComboBurstSounds[i] = Integer.parseInt(split[i]);
                                        skin.customComboBurstSounds = customComboBurstSounds;
                                        break;
                                    case "ComboBurstRandom":
                                        skin.comboBurstRandom = BaseUtils.parseBoolean(tokens[1]);
                                        break;
                                    case "SliderStyle":
                                        skin.sliderStyle = Byte.parseByte(tokens[1]);
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                System.out.println(
                                        String.format("Failed to read line '%s' for file '%s'.",
                                                line, skinFile.path())
                                );
                            }
                        }
                        break;
                    case "[Colours]":
                        Array<Color> colors = new Array<>();
                        while ((line = in.readLine()) != null) {
                            line = line.trim();
                            if (isLineInvalid(line))
                                continue;
                            if (line.charAt(0) == '[')
                                break;
                            if ((tokens = tokenize(line)) == null)
                                continue;
                            try {
                                String[] rgb = tokens[1].split(",");
                                Color color = new Color(
                                        Integer.parseInt(rgb[0].trim()),
                                        Integer.parseInt(rgb[1].trim()),
                                        Integer.parseInt(rgb[2].trim()),
                                        1
                                );
                                switch (tokens[0]) {
                                    case "Combo1":
                                    case "Combo2":
                                    case "Combo3":
                                    case "Combo4":
                                    case "Combo5":
                                    case "Combo6":
                                    case "Combo7":
                                    case "Combo8":
                                        colors.add(color);
                                        break;
                                    case "MenuGlow":
                                        skin.menuGlow = color;
                                        break;
                                    case "SliderBorder":
                                        skin.sliderBorder = color;
                                        break;
                                    case "SliderBall":
                                        skin.sliderBall = color;
                                        break;
                                    case "SpinnerApproachCircle":
                                        skin.spinnerApproachCircle = color;
                                        break;
                                    case "SongSelectActiveText":
                                        skin.songSelectActiveText = color;
                                        break;
                                    case "SongSelectInactiveText":
                                        skin.songSelectInactiveText = color;
                                        break;
                                    case "StarBreakAdditive":
                                        skin.starBreakAdditive = color;
                                        break;
                                    case "InputOverlayText":
                                        skin.inputOverlayText = color;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                System.out.println(
                                        String.format("Failed to read color '%s' for file '%s'.",
                                                line, skinFile.path())
                                );
                            }
                        }
                        if (!colors.isEmpty())
                            skin.combo = colors.toArray();
                        break;
                    case "[Fonts]":
                        while ((line = in.readLine()) != null) {
                            line = line.trim();
                            if (isLineInvalid(line))
                                continue;
                            if (line.charAt(0) == '[')
                                break;
                            if ((tokens = tokenize(line)) == null)
                                continue;
                            try {
                                switch (tokens[0]) {
                                    case "HitCirclePrefix":
                                        skin.hitCirclePrefix = tokens[1];
                                        break;
                                    case "HitCircleOverlap":
                                        skin.hitCircleOverlap = Integer.parseInt(tokens[1]);
                                        break;
                                    case "ScorePrefix":
                                        skin.scorePrefix = tokens[1];
                                        break;
                                    case "ScoreOverlap":
                                        skin.scoreOverlap = Integer.parseInt(tokens[1]);
                                        break;
                                    case "ComboPrefix":
                                        skin.comboPrefix = tokens[1];
                                        break;
                                    case "ComboOverlap":
                                        skin.comboOverlap = Integer.parseInt(tokens[1]);
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                System.out.println((String.format("Failed to read color '%s' for file '%s'.",
                                        line, skinFile.path())));
                            }
                        }
                        break;
                    default:
                        line = in.readLine();
                        break;
                }
            }
        } catch (IOException ignore) { }

        return skin;
    }

    /**
     * Returns false if the line is too short or commented.
     */
    private static boolean isLineInvalid(String line) {
        return (line.length() <= 1 || line.startsWith("//"));
    }

    /**
     * Splits line into two strings: tag, value.
     * If no ':' character is present, null will be returned.
     */
    private static String[] tokenize(String line) {
        int index = line.indexOf(':');
        if (index == -1) {
            System.out.println((String.format("Failed to tokenize line: '%s'.", line)));
            return null;
        }

        String[] tokens = new String[2];
        tokens[0] = line.substring(0, index).trim();
        tokens[1] = line.substring(index + 1).trim();

        return tokens;
    }
}