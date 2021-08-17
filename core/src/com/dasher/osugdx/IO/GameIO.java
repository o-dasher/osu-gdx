package com.dasher.osugdx.IO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.jetbrains.annotations.NotNull;


public class GameIO {
    private String baseDirectoryName;
    private String mainDirectoryPath;
    private FileHandle importDir;
    private FileHandle songsDir;

    public void createDir(@NotNull FileHandle fileHandle) {
        if (fileHandle.exists()) {
            System.out.println(fileHandle.path() + " directory was already created!");
            if (!fileHandle.isDirectory()) {
                fileHandle.delete();
            }
        } else {
            System.out.println("Creating new directory...");
            fileHandle.mkdirs();
            System.out.println("Created game directory: " + fileHandle.path());
        }
    }

    public void setup(String baseDirectoryName) {
        this.baseDirectoryName = baseDirectoryName;
        this.mainDirectoryPath =  Gdx.files.getExternalStoragePath() + baseDirectoryName;

        System.out.println("MAIN EXTERNAL DIRECTORY: " + getMainDirectoryPath());

        createDir(importDir = getFileHandle("Import"));
        createDir(songsDir = getFileHandle("Songs"));
    }

    private @NotNull String getMainDirectoryPath() {
        return mainDirectoryPath;
    }

    public FileHandle getFileHandle(String path) {
        return Gdx.files.external(baseDirectoryName + "/" + path);
    }

    public FileHandle getImportDir() {
        return importDir;
    }

    public FileHandle getSongsDir() {
        return songsDir;
    }
}
