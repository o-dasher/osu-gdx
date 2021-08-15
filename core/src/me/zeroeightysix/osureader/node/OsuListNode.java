package me.zeroeightysix.osureader.node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuListNode extends AbstractMappedNode<List<Object>> {

    private final List<Object> value;

    public OsuListNode(String key, List<Object> value) {
        super(key);
        this.value = value;
    }

    @Override
    public List<Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getKey() + ": " + String.join(",", getValue().stream().map(Object::toString).collect(Collectors.toList()));
    }
}
