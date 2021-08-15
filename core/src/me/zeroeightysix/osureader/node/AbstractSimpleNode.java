package me.zeroeightysix.osureader.node;

/**
 * Created by 086 on 16/05/2018.
 */
public abstract class AbstractSimpleNode<S> implements OsuNode<S> {

    protected final S value;

    public AbstractSimpleNode(S value) {
        this.value = value;
    }

    @Override
    public S getValue() {
        return value;
    }

}
