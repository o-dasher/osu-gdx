package com.dasher.osugdx.IO;

import java.io.Closeable;
import java.io.IOException;

public class IOHelper {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
