package com.dasher.osugdx.osu.Beatmaps;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dasher.osugdx.Framework.Interfaces.Listenable;
import com.dasher.osugdx.IO.GameIO;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Class for decompressing *.osz files and copying files into application
 * directory;
 **/
public class OSZParser implements Listenable<OSZParserListener>, OSZParserListener {
    private final BeatMapStore beatMapStore;
    private final BeatmapManager beatmapManager;
    private final FileHandle beatmapsFolder;
    private final FileHandle importsFolder;
    private boolean isImportingImportDirectory;
    private final Array<BeatMapSet> newImportedBeatmapSets = new Array<>();
    private final Array<OSZParserListener> listeners = new Array<>();

    public OSZParser(@NotNull GameIO gameIO, BeatMapStore beatMapStore, BeatmapManager beatmapManager) {
        this.beatmapsFolder = gameIO.getSongsDir();
        this.importsFolder = gameIO.getImportDir();
        this.beatmapManager = beatmapManager;
        this.beatMapStore = beatMapStore;
    }

    public void parseImportDirectory() {
        isImportingImportDirectory = true;
        for (FileHandle file: importsFolder.list(pathname -> pathname.getName().endsWith("osz"))) {
            System.out.println("Importing: " + file.nameWithoutExtension());
            if (parseOSZ(file) != null) {
                System.out.println(file.nameWithoutExtension() + " imported!");
            }
        }
        if (newImportedBeatmapSets.notEmpty()) {
            beatmapManager.setCurrentBeatmapSet(newImportedBeatmapSets.get(newImportedBeatmapSets.size  - 1));
        }
        // TODO: MAKE PARSE END SEND AN ARRAY OF THE NEW IMPORTED BEATMAPSETS
        onParseEnd(newImportedBeatmapSets);
        newImportedBeatmapSets.clear();
        isImportingImportDirectory = false;
    }

    public String getFolderPathFor(@NotNull FileHandle oszFile) {
       return beatmapsFolder.path() + "/" + oszFile.nameWithoutExtension();
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
        Array<File> list = new Array<>();
        String folderPath = getFolderPathFor((beatmapSetOsz));
        final FileHandle folderFile = Gdx.files.external(folderPath);
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
            newImportedBeatmapSets.add(beatMapStore.loadBeatmapSet(Gdx.files.external(folderPath)));
        }

        return folderFile;
    }

    public boolean isImportingImportDirectory() {
        return isImportingImportDirectory;
    }

    @Override
    public Array<OSZParserListener> getListeners() {
        return listeners;
    }

    @Override
    public void onParseEnd(Array<BeatMapSet> newImportedBeatmapSets) {
        for (OSZParserListener listener: listeners) {
            listener.onParseEnd(newImportedBeatmapSets);
        }
    }
}