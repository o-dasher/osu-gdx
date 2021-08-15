package me.zeroeightysix.osureader.section;

/**
 * Created by 086 on 16/05/2018.
 */
public class MetadataSection extends Section {

    @Source("Title") private String title;
    @Source("TitleUnicode") private String titleUnicode;
    @Source("Artist") private String artist;
    @Source("ArtistUnicode") private String artistUnicode;
    @Source("Creator") private String creator;
    @Source("Version") private String version;
    @Source("Source") private String source;
    @Source("Tags") private String tags;
    @Source("BeatmapID") private int beatmapID;
    @Source("BeatmapSetID") private int beatmapSetID;

    public MetadataSection(OsuSection source) throws IllegalAccessException {
        super(source);
    }

    public String getArtist() {
        return artist;
    }

    public String getTags() {
        return tags;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public String getCreator() {
        return creator;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public String getVersion() {
        return version;
    }

    public int getBeatmapID() {
        return beatmapID;
    }

    public int getBeatmapSetID() {
        return beatmapSetID;
    }

}
