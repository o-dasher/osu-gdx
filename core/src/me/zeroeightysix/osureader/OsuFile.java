package me.zeroeightysix.osureader;

import me.zeroeightysix.osureader.node.OsuVersionNode;
import me.zeroeightysix.osureader.section.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
public class OsuFile implements Serializable {

    /**
     * The osu file format version of this file
     */
    private final int version;
    /**
     * A list of all sections in this osu file
     */
    private final List<OsuSection> sectionList;

    public OsuFile(int version, List<OsuSection> sectionList) {
        this.version = version;
        this.sectionList = sectionList;
    }

    public int getVersion() {
        return version;
    }

    public List<OsuSection> getSectionList() {
        return sectionList;
    }

    public GeneralSection getGeneral() {
        try {
            return new GeneralSection(sectionList.stream().filter(section -> section.getName().equals("General")).findAny().orElse(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EditorSection getEditor() {
        try {
            return new EditorSection(sectionList.stream().filter(section -> section.getName().equals("Editor")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MetadataSection getMetadata() {
        try {
            return new MetadataSection(sectionList.stream().filter(section -> section.getName().equals("Metadata")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DifficultySection getDifficulty() {
        try {
            return new DifficultySection(sectionList.stream().filter(section -> section.getName().equals("Difficulty")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EventsSection getEvents() {
        try {
            return new EventsSection(sectionList.stream().filter(section -> section.getName().equals("Events")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TimingPointsSection getTimingPoints() {
        try {
            return new TimingPointsSection(sectionList.stream().filter(section -> section.getName().equals("TimingPoints")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ColoursSection getColours() {
        try {
            return new ColoursSection(sectionList.stream().filter(section -> section.getName().equals("Colours")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HitObjectsSection getHitObjects() {
        try {
            return new HitObjectsSection(sectionList.stream().filter(section -> section.getName().equals("HitObjects")).findAny().orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s%d\n\n", OsuVersionNode.PREFIX, version) + String.join("\n\n", getSectionList().stream().map(OsuSection::toString).collect(Collectors.toList()));
    }
}
