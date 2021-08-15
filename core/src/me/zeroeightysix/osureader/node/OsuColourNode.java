package me.zeroeightysix.osureader.node;

import java.io.Serializable;

import me.zeroeightysix.osureader.OsuReader;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuColourNode extends AbstractMappedNode<OsuReader.Colour> {
    private final OsuReader.Colour colour;

    public OsuColourNode(String key, OsuReader.Colour colour) {
        super(key);
        this.colour = colour;
    }

    @Override
    public OsuReader.Colour getValue() {
        return colour;
    }
}
