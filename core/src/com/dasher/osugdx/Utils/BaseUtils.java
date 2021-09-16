package com.dasher.osugdx.Utils;

public abstract class BaseUtils {
    private BaseUtils() {}

    /**
     * Parses the integer string argument as a boolean:
     * {@code 1} is {@code true}, and all other values are {@code false}.
     * @param s the {@code String} containing the boolean representation to be parsed
     * @return the boolean represented by the string argument
     */
    public static boolean parseBoolean(String s) {
        return (Integer.parseInt(s) == 1);
    }
}
