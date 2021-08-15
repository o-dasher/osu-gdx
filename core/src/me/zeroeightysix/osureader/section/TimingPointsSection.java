package me.zeroeightysix.osureader.section;

import me.zeroeightysix.osureader.node.OsuNode;
import me.zeroeightysix.osureader.node.OsuPrimitiveNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 086 on 16/05/2018.
 */
public class TimingPointsSection {

    List<TimingPoint> timingPoints = new LinkedList<>();

    public TimingPointsSection(OsuSection section) {
        for (OsuNode node : section.getEntries()) {
            if (node instanceof OsuPrimitiveNode) {
                Object o = node.getValue();
                if (o instanceof List) {
                    long offset = ((Number) ((List) o).get(0)).longValue();
                    double msPerBeat = ((Number) ((List) o).get(1)).doubleValue();
                    int meter = ((Number) ((List) o).get(2)).intValue();
                    GeneralSection.SampleSet sampleSet = GeneralSection.SampleSet.byOrdinal(((Number) ((List) o).get(3)).intValue());
                    int sampleIndex = ((Number) ((List) o).get(4)).intValue();
                    int volume = ((Number) ((List) o).get(5)).intValue();
                    boolean inherited = ((Number) ((List) o).get(6)).intValue() != 0;
                    boolean kiai = ((Number) ((List) o).get(7)).intValue() != 0;
                    timingPoints.add(new TimingPoint(offset, msPerBeat, meter, sampleSet, sampleIndex, volume, inherited, kiai));
                }
            }
        }
    }

    public static class TimingPoint {
        private long offset;
        private double msPerBeat;
        private int meter;
        private GeneralSection.SampleSet sampleSet;
        private int sampleIndex;
        private int volume;
        private boolean inherited;
        private boolean kiai;

        public TimingPoint(long offset, double msPerBeat, int meter, GeneralSection.SampleSet sampleSet, int sampleIndex, int volume, boolean inherited, boolean kiai) {
            this.offset = offset;
            this.msPerBeat = msPerBeat;
            this.meter = meter;
            this.sampleSet = sampleSet;
            this.sampleIndex = sampleIndex;
            this.volume = volume;
            this.inherited = inherited;
            this.kiai = kiai;
        }

        public GeneralSection.SampleSet getSampleSet() {
            return sampleSet;
        }

        public double getMsPerBeat() {
            return msPerBeat;
        }

        public int getMeter() {
            return meter;
        }

        public int getSampleIndex() {
            return sampleIndex;
        }

        public int getVolume() {
            return volume;
        }

        public long getOffset() {
            return offset;
        }

        public boolean isInherited() {
            return inherited;
        }

        public boolean isKiai() {
            return kiai;
        }
    }

}
