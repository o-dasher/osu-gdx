package me.zeroeightysix.osureader.section;

import me.zeroeightysix.osureader.node.OsuNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuSection implements Serializable {

    private String name;
    private List<OsuNode> entries;

    public OsuSection(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<OsuNode> getEntries() {
        return entries;
    }

    public void add(OsuNode node) {
        this.entries.add(node);
    }

    public void addAll(Collection<OsuNode> entries) {
        this.entries.addAll(entries);
    }

    /**
     * A list of all sections you should find in a standard osu file
     */
    public static final String[] STANDARD_SECTIONS = {
            "General",
            "Editor",
            "Metadata",
            "Difficulty",
            "Events",
            "TimingPoints",
            "Colours",
            "HitObjects",
    };

    @Override
    public String toString() {
        return String.format("[%s]\n", name) + String.join(System.lineSeparator(), entries.stream().map(Object::toString).collect(Collectors.toList()));
    }
}
