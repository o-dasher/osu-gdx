package me.zeroeightysix.osureader.section;

/**
 * Created by 086 on 16/05/2018.
 */
public class GeneralSection extends Section {

    @Source("AudioFilename") private String audioFilename;
    @Source("AudioLeadIn") private int audioLeadIn;
    @Source("PreviewTime") private int previewTime;
    @Source("Countdown") private boolean countdown;
    @Source("SampleSet") private SampleSet sampleSet;
    @Source("StackLeniency") private double stackLeniency;
    @Source("Mode") private Mode mode;
    @Source("LetterboxInBreaks") private boolean letterboxInBreaks;
    @Source("WidescreenStoryboard") private boolean widescreenStoryboard;

    public GeneralSection(OsuSection source) throws IllegalAccessException {
        super(source);
    }

    public double getStackLeniency() {
        return stackLeniency;
    }

    public int getAudioLeadIn() {
        return audioLeadIn;
    }

    public int getPreviewTime() {
        return previewTime;
    }

    public Mode getMode() {
        return mode;
    }

    public SampleSet getSampleSet() {
        return sampleSet;
    }

    public String getAudioFilename() {
        return audioFilename;
    }

    public boolean isCountdown() {
        return countdown;
    }

    public boolean isLetterboxInBreaks() {
        return letterboxInBreaks;
    }

    public boolean isWidescreenStoryboard() {
        return widescreenStoryboard;
    }

    public static enum SampleSet {
        NONE, AUTO, NORMAL, SOFT, DRUM;

        private static final SampleSet[] set = values();
        public static SampleSet byOrdinal(int ordinal) {
            return set[ordinal];
        }
    }

    public static enum Mode {
        OSU, TAIKO, CATCHTHEBEAT, OSUMANIA;

        private static final Mode[] set = values();
        public static Mode byOrdinal(int ordinal) {
            return set[ordinal];
        }
    }

}
