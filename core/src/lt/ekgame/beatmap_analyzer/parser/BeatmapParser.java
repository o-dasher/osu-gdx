package lt.ekgame.beatmap_analyzer.parser;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Collections;
import com.dasher.osugdx.IO.IOHelper;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lt.ekgame.beatmap_analyzer.GameMode;
import lt.ekgame.beatmap_analyzer.beatmap.*;
import lt.ekgame.beatmap_analyzer.parser.hitobjects.*;

public class BeatmapParser {
	private final Pattern PART_TAG = Pattern.compile("^\\[(\\w+)]");
	private final String[] REQUIRED_TAGS = {"General", "Metadata", "TimingPoints", "Difficulty", "Events", "HitObjects"};
	
	private final Map<GameMode, HitObjectParser<?>> PARSERS = new HashMap<>();

	public BeatmapParser() {
		PARSERS.put(GameMode.OSU,   new OsuParser());
		PARSERS.put(GameMode.TAIKO, new TaikoParser());
		PARSERS.put(GameMode.CATCH, new CatchParser());
		PARSERS.put(GameMode.MANIA, new ManiaParser());
	}

	public Beatmap parse(File file) throws FileNotFoundException, BeatmapException {
		return parse(new FileInputStream(file));
	}
	
	public Beatmap parse(String string) throws BeatmapException {
		return parse(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
	}
	
	public Beatmap parse(InputStream stream) throws BeatmapException {
		try (Scanner scanner = new Scanner(stream)) {
			Map<String, FilePart> parts = new HashMap<>();
			
			String tag = "Header";
			Array<String> lines = new Array<>();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				Matcher matcher = PART_TAG.matcher(line);
				if (matcher.find()) {
					parts.put(tag, new FilePart(tag, lines));
					lines = new Array<>();
					tag = matcher.group(1);
				} else if (!line.isEmpty() && !line.startsWith("//")){
					lines.add(line);
				}
			}
			parts.put(tag, new FilePart(tag, lines));
			
			for (String reqiredTag : REQUIRED_TAGS)
				if (!parts.containsKey(reqiredTag))
					throw new BeatmapException("Couldn't find required \"" + reqiredTag + "\" tag found.");
			
			BeatmapGenerals generalSettings = new BeatmapGenerals(parts.get("General"));
			HitObjectParser<?> parser = PARSERS.get(generalSettings.getGamemode());
			// TODO: parse other gamemodes
			if (parser == null)
				return null;
			
			BeatmapMetadata metadata = new BeatmapMetadata(parts.get("Metadata"));
			BeatmapDifficulties difficulties = new BeatmapDifficulties(parts.get("Difficulty"));
			BeatmapEditorState editorState = null;
			
			// Older formats don't have the "Editor" tag
			if (parts.containsKey("Editor"))
				editorState = new BeatmapEditorState(parts.get("Editor"));

			Array<BreakPeriod> breaks = parseBreaks(parts.get("Events"));
			Array<TimingPoint> timingPoints = parseTimePoints(parts.get("TimingPoints"));
			Array<String> rawObjects = parts.get("HitObjects").getLines();
			IOHelper.close(stream);
			
			return parser.buildBeatmap(generalSettings, editorState, metadata, difficulties, breaks, timingPoints, rawObjects);
		}
	}

	private Array<TimingPoint> parseTimePoints(FilePart part) {
		Array<TimingPoint> timingPoints = new Array<>();
		for (String string: part.getLines()) {
			String[] args = string.split(",");

			double timestamp = Double.parseDouble(args[0].trim());
			double beatLength = Double.parseDouble(args[1].trim());
			int meter = 4;
			int sampleType = 0;
			int sampleSet = 0;
			int volume = 100;
			boolean isInherited = false;
			boolean isKiai = false;

			if (args.length > 2) {
				// DASHER
				try {
					meter = Integer.parseInt(args[2].trim());
					sampleType = Integer.parseInt(args[3].trim());
					sampleSet = Integer.parseInt(args[4].trim());
					volume = Integer.parseInt(args[5].trim());
				} catch (NumberFormatException ignore) {}
			}

			if (args.length >= 7)
				isInherited = Integer.parseInt(args[6].trim()) == 0;
			if (args.length >= 8)
				isKiai = Integer.parseInt(args[7].trim()) == 0;

			timingPoints.add(new TimingPoint(timestamp, beatLength, meter, sampleType, sampleSet, volume, isInherited, isKiai));
		}
		return timingPoints;
	}
	
	private @NotNull Array<BreakPeriod> parseBreaks(@NotNull FilePart part) {
		Array<BreakPeriod> breaks = new Array<>();
		for (String string: part.getLines()) {
			if (string.trim().startsWith("2, ")) {
				String[] args = string.split(",");
				 breaks.add(new BreakPeriod(
						Integer.parseInt(args[1].trim()),
						Integer.parseInt(args[2].trim())
				));
			}
		}
		return breaks;
	}
}
