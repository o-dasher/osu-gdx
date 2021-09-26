package com.dasher.osugdx.osu.Beatmaps;

import com.badlogic.gdx.utils.Array;

public interface OSZParserListener {
    void onParseEnd(Array<BeatMapSet> newImportedBeatmapSets);
}
