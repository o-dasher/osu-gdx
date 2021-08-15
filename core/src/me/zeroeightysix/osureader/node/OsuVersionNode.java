package me.zeroeightysix.osureader.node;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuVersionNode extends OsuNumberNode {
    public static final String PREFIX = "osu file format v";

    public OsuVersionNode(double value) {
        super("", value);
    }

    @Override
    public String getKey() {
        return null;
    }

    public int getVersion() {
        return getValue().intValue();
    }

    @Override
    public String toString() {
        return PREFIX + getVersion();
    }
}
