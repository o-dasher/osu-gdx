package lt.ekgame.beatmap_analyzer.parser.hitobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import lt.ekgame.beatmap_analyzer.beatmap.*;
import lt.ekgame.beatmap_analyzer.beatmap.mania.*;


public class ManiaParser extends HitObjectParser<ManiaObject> {

	@Override
	public ManiaObject parse(String line) {
		String[] args = line.split(",");
		Vector2 position = new Vector2(
			Integer.parseInt(args[0].trim()),
			Integer.parseInt(args[1].trim())
		);
		int time = Integer.parseInt(args[2].trim());
		int type = Integer.parseInt(args[3].trim());
		int hitSound = Integer.parseInt(args[4].trim());
				
		if ((type & 1) > 0) {
			return new ManiaSingle(position, time, hitSound);
		}
		else {
			String[] additions = args[5].split(":");
			int endTime = Integer.parseInt(additions[0].trim());
			return new ManiaHold(position, time, endTime, hitSound);
		}
	}

	@Override
	public Beatmap buildBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState, BeatmapMetadata metadata,
			BeatmapDifficulties difficulties, Array<BreakPeriod> breaks, Array<TimingPoint> timingPoints,
			Array<String> rawObjects) {
		Array<ManiaObject> hitObjects = parse(rawObjects);
		return new ManiaBeatmap(generals, editorState, metadata, difficulties, breaks, timingPoints, hitObjects);
	}
}
