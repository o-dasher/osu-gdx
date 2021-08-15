package me.zeroeightysix.osureader.node;

import java.io.Serializable;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuPrimitiveNode extends AbstractSimpleNode {
    public OsuPrimitiveNode(Object value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
