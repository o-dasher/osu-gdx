package lt.ekgame.beatmap_analyzer.parser.hitobjects;

import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapDifficulties;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapEditorState;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapGenerals;
import lt.ekgame.beatmap_analyzer.beatmap.BeatmapMetadata;
import lt.ekgame.beatmap_analyzer.beatmap.BreakPeriod;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;

public abstract class HitObjectParser<T extends HitObject> {
	
	public abstract T parse(String line);
	
	public Array<T> parse(@NotNull Array<String> lines) {
		Array<T> items = new Array<>();
		for (String item: lines) {
			items.add(parse(item));
		}
		return items;
	}
	
	public abstract Beatmap buildBeatmap(BeatmapGenerals generals, BeatmapEditorState editorState,
		 BeatmapMetadata metadata, BeatmapDifficulties difficulties, Array<BreakPeriod> breaks,
		 Array<TimingPoint> timingPoints, Array<String> rawObjects);
	
}
