package me.zeroeightysix.osureader.node;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuSectionNode implements OsuNode<String> {

    private final String title;

    public OsuSectionNode(String title) {
        this.title = title;
    }

    @Override
    public String getValue() {
        return title;
    }
}
