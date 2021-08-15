package me.zeroeightysix.osureader;

import me.zeroeightysix.osureader.parse.OsuFileParser;
import me.zeroeightysix.osureader.parse.StandardOsuFileParser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * Created by 086 on 16/05/2018.
 *
 * Utility class for parsing osu files
 */
public class OsuReader {

    public static OsuFile read(File file, OsuFileParser parser) throws IOException {
        return parser.parse(file);
    }

    public static OsuFile read(File file) throws IOException {
        return read(file, new StandardOsuFileParser());
    }

    public static OsuFile read(Path file, OsuFileParser parser) throws IOException {
        return parser.parse(file);
    }

    public static OsuFile read(Path file) throws IOException {
        return read(file, new StandardOsuFileParser());
    }

    public static class Colour implements Serializable {
        int r, g, b;

        public Colour(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        @Override
        public String toString() {
            return String.format("Colour(%d,%d,%d)", getR(), getG(), getB());
        }
    }

}
