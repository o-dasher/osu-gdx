package lt.ekgame.beatmap_analyzer.parser.hitobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


import lt.ekgame.beatmap_analyzer.beatmap.*;
import lt.ekgame.beatmap_analyzer.beatmap.osu.*;

public class OsuParser extends HitObjectParser<OsuObject> {

	@Override
	public OsuObject parse(String line) {
		String[] args = line.split(",");
		Vector2 position = new Vector2(
			Integer.parseInt(args[0].trim()),
			Integer.parseInt(args[1].trim())
		);
		int time = Integer.parseInt(args[2].trim());
		int type = Integer.parseInt(args[3].trim());
		int hitSound = Integer.parseInt(args[4].trim());
		boolean newCombo = (type & 4) > 0;
		
		if ((type & 1) > 0) {
			return new OsuCircle(position, time, hitSound, newCombo);
		}
		else if ((type & 2) > 0) {
			String[] sliderData = args[5].trim().split("\\|");
			
			char sliderTypeChar = sliderData[0].charAt(0);
			OsuSlider.SliderType sliderType = OsuSlider.SliderType.fromChar(sliderTypeChar);
			
			Array<Vector2> sliderPoints = new Array<>();
			
			for (int i = 1; i < sliderData.length; i++) {
				String[] pointData = sliderData[i].split(":");
				sliderPoints.add(new Vector2(
					Integer.parseInt(pointData[0]),
					Integer.parseInt(pointData[1])
				));
			}

			int repetitions = Integer.parseInt(args[6].trim());
			double pixelLength = Double.parseDouble(args[7].trim());
			
			return new OsuSlider(position, time, hitSound, newCombo, sliderType, sliderPoints, repetitions, pixelLength);
		}
		else {
			int endTime = Integer.parseInt(args[5].trim());
			return new OsuSpinner(position, time, endTime, hitSound, newCombo);
		}
	}

	@Override
	public Beatmap buildBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState,
			BeatmapMetadata metadata, BeatmapDifficulties difficulties, Array<BreakPeriod> breaks,
			Array<TimingPoint> timingPoints, Array<String> rawObjects)
	{
		Array<OsuObject> hitObjects = parse(rawObjects);
		return new OsuBeatmap(generals, editorState, metadata, difficulties, breaks, timingPoints, hitObjects);
	}

}
