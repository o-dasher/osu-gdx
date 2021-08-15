package me.zeroeightysix.osureader.node;


/**
 * Created by 086 on 16/05/2018.
 */
public class OsuNumberNode extends AbstractMappedNode<Number>  {

    private final Number value;
    boolean isInt;

    public OsuNumberNode(String key, double value) {
        super(key);
        this.value = value;
        this.isInt = this.value.intValue()==this.value.doubleValue();
    }

    @Override
    public Number getValue() {
        return value;
    }

    public boolean isInt() {
        return isInt;
    }

    public double getValueDouble() {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return getKey() + ": " + (isInt() ? String.format("%s", value.intValue()) : value.doubleValue());
    }

}
