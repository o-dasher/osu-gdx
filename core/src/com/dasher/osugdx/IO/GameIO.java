package com.dasher.osugdx.IO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;

public class GameIO extends LinkedList<FileHandle> {
    private String baseDirectoryName;
    private String mainDirectoryPath;
    private FileHandle importDir;
    private FileHandle songsDir;

    @Override
    public boolean add(FileHandle fileHandle) {
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
        return super.add(fileHandle);
    }

    public void setup(String baseDirectoryName) {
        this.baseDirectoryName = baseDirectoryName;
        this.mainDirectoryPath =  Gdx.files.getExternalStoragePath() + baseDirectoryName;

        System.out.println("MAIN EXTERNAL DIRECTORY: " + getMainDirectoryPath());

        add(importDir = getFileHandle("Import"));
        add(songsDir = getFileHandle("Songs"));
    }

    private @NotNull String getMainDirectoryPath() {
        return mainDirectoryPath;
    }

    public FileHandle getFileHandle(String path) {
        return Gdx.files.external(baseDirectoryName + "/" + path);
    }

    @Contract("_ -> new")
    public @NotNull File getJavaFile(@NotNull FileHandle fileHandle) {
        return getJavaFile(fileHandle.path());
    }

    public File getJavaFile(String path) {
        return new File(Gdx.files.getExternalStoragePath() + path);
    }

    public FileHandle getImportDir() {
        return importDir;
    }

    public FileHandle getSongsDir() {
        return songsDir;
    }
}
