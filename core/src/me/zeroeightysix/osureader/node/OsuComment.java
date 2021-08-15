package me.zeroeightysix.osureader.node;

import java.io.Serializable;

/**
 * Created by 086 on 16/05/2018.
 * A comment.
 */
public class OsuComment implements OsuNode<String>{
    public static final String PREFIX = "//";

    private final String comment;

    public OsuComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getValue() {
        return comment;
    }

    @Override
    public String toString() {
        return PREFIX + getValue();
    }
}
