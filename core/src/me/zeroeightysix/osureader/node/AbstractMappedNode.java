package me.zeroeightysix.osureader.node;

/**
 * Created by 086 on 16/05/2018.
 */
public abstract class AbstractMappedNode<T> implements OsuMappedNode<T> {

    private final String key;

    public AbstractMappedNode(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return getKey() + ": " + getValue();
    }

}
