package com.dasher.osugdx.IO.Beatmaps;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.dasher.osugdx.IO.GameIO;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Class for decompressing *.osz files and copying files into application
 * directory;
 **/
public class OSZParser {
    private final BeatMapStore beatMapStore;
    private final FileHandle beatmapsFolder;
    private final FileHandle importsFolder;

    public OSZParser(@NotNull GameIO gameIO, BeatMapStore beatMapStore) {
        this.beatmapsFolder = gameIO.getSongsDir();
        this.importsFolder = gameIO.getImportDir();
        this.beatMapStore = beatMapStore;
    }

    public void parseImportDirectory() {
        for (FileHandle file: importsFolder.list(pathname -> pathname.getName().endsWith("osz"))) {
            System.out.println("Importing: " + file.nameWithoutExtension());
            if (parseOSZ(file) != null) {
                System.out.println(file.nameWithoutExtension() + " imported!");
            }
        }
    }

    /**
     * Parse an *.osz file. Location of decompressed files depends of settings
     * and storage availability.
     * return: The osz folder file in external storage
     */
    public FileHandle parseOSZ(FileHandle beatmapSetOsz) {
        if (!beatmapSetOsz.extension().equals("osz")) {
            return null;
        }

        System.out.println("Importing BeatmapSet: " + beatmapSetOsz);
        String folderName = beatmapSetOsz.nameWithoutExtension();
        Array<File> list = new Array<>();
        final FileHandle folderFile = Gdx.files.external(beatmapsFolder.path() + "/" + folderName);
        folderFile.mkdirs();
        list.add(folderFile.file());

        ZipInputStream istream;
        istream = new ZipInputStream(beatmapSetOsz.read());

        try {
            for (ZipEntry entry = istream.getNextEntry(); entry != null; entry = istream.getNextEntry()) {
                final File entryFile = new File(folderFile.file(), entry.getName());
                if (entryFile.getParent() != null) {
                    final File entryFolder = new File(entryFile.getParent());
                    if (!entryFolder.exists()) {
                        if (entryFolder.mkdirs()) {
                            list.add(entryFile);
                        }
                    }
                }
                if (entryFile.createNewFile()) {
                    list.add(entryFile);
                }
                final FileOutputStream entryStream = new FileOutputStream(entryFile);

                // Writing data from zip stream to output stream
                final byte[] buff = new byte[4096];
                int len;
                while ((len = istream.read(buff)) > 0) {
                    entryStream.write(buff, 0, len);
                }
                entryStream.close();
            }
            istream.close();
        } catch (IOException e) {
            System.out.println("OSZParser.ParseOSZ: " + e.getMessage());
            beatmapSetOsz.delete();
            try {
                istream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }

        if (beatmapSetOsz.type() == Files.FileType.External) {
            beatmapSetOsz.delete();
            beatMapStore.loadBeatmapSet(Gdx.files.external(beatmapsFolder.path() + folderName));
        }

        return folderFile;
    }
}