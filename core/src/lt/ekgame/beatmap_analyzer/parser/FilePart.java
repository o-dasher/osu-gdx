package lt.ekgame.beatmap_analyzer.parser;

import com.badlogic.gdx.utils.Array;

import java.util.List;

public class FilePart {

	private String tag;
	private Array<String> lines;
	
	FilePart(String tag, Array<String> lines) {
		this.tag = tag;
		this.lines = lines;
	}
	
	public String getTag() {
		return tag;
	}

	public Array<String> getLines() {
		return lines;
	}
}
