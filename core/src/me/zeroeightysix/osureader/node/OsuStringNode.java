package me.zeroeightysix.osureader.node;

import java.io.Serializable;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuStringNode extends AbstractMappedNode<String> {

    private final String value;

    public OsuStringNode(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
