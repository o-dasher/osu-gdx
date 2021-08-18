package lt.ekgame.beatmap_analyzer.beatmap;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lt.ekgame.beatmap_analyzer.parser.BeatmapException;
import lt.ekgame.beatmap_analyzer.parser.FilePart;
import lt.ekgame.beatmap_analyzer.parser.FilePartConfig;

public class BeatmapMetadata {
	
	private String title;
	private String titleRomanized;
	private String artist;
	private String artistRomanized;
	private String creator;
	private String version;
	private String source;
	private Array<String> tags = new Array<>();
	private String beatmapId;
	private String beatmapSetId;
	
	private BeatmapMetadata() {}
	
	public BeatmapMetadata(FilePart part) throws BeatmapException {
		FilePartConfig config = new FilePartConfig(part);
		titleRomanized = config.getString("Title");
		title = config.getString("TitleUnicode", titleRomanized);
		artistRomanized = config.getString("Artist");
		artist = config.getString("ArtistUnicode", artistRomanized);
		creator = config.getString("Creator");
		version = config.getString("Version");
		source = config.getString("Source", "");
		tags = new Array<>(config.getString("Tags", "").split(" "));
		beatmapId = config.getString("BeatmapID", null);
		beatmapSetId = config.getString("BeatmapSetID", null);
	}
	
	public BeatmapMetadata clone() {
		BeatmapMetadata clone = new BeatmapMetadata();
		clone.titleRomanized = this.titleRomanized;
		clone.title = this.title;
		clone.artistRomanized = this.artistRomanized;
		clone.artist = this.artist;
		
		clone.creator = this.creator;
		clone.version = this.version;
		clone.source = this.source;
		clone.tags = this.tags;
		clone.beatmapId = this.beatmapId;
		clone.beatmapSetId = this.beatmapSetId;
		return clone;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleRomanized() {
		return titleRomanized;
	}

	public String getArtist() {
		return artist;
	}

	public String getArtistRomanized() {
		return artistRomanized;
	}

	public String getCreator() {
		return creator;
	}

	public String getVersion() {
		return version;
	}

	public String getSource() {
		return source;
	}

	public Array<String> getTags() {
		return tags;
	}

	public String getBeatmapId() {
		return beatmapId;
	}

	public String getBeatmapSetId() {
		return beatmapSetId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleRomanized(String titleRomanized) {
		this.titleRomanized = titleRomanized;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setArtistRomanized(String artistRomanized) {
		this.artistRomanized = artistRomanized;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTags(Array<String> tags) {
		this.tags = tags;
	}

	public void setBeatmapId(String beatmapId) {
		this.beatmapId = beatmapId;
	}

	public void setBeatmapSetId(String beatmapSetId) {
		this.beatmapSetId = beatmapSetId;
	}
}
