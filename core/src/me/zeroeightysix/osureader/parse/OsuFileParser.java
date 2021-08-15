package me.zeroeightysix.osureader.parse;

import me.zeroeightysix.osureader.OsuFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by 086 on 16/05/2018.
 */
public interface OsuFileParser {

    public OsuFile parse(File file) throws IOException;
    public OsuFile parse(Path file) throws IOException;

}
