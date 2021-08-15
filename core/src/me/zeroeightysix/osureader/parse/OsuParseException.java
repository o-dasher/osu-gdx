package me.zeroeightysix.osureader.parse;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuParseException extends RuntimeException {
    public OsuParseException(String message) {
        super(message);
    }

    public OsuParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public OsuParseException(Throwable cause) {
        super(cause);
    }
}
