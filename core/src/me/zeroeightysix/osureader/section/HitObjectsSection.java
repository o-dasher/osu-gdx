package me.zeroeightysix.osureader.section;

import me.zeroeightysix.osureader.node.OsuNode;
import me.zeroeightysix.osureader.node.OsuPrimitiveNode;
import me.zeroeightysix.osureader.parse.OsuParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 086 on 16/05/2018.
 */
public class HitObjectsSection {

    List<HitObject> objects = new ArrayList<>();

    public HitObjectsSection(OsuSection section) {
        for (OsuNode node : section.getEntries()) {
            if (node instanceof OsuPrimitiveNode) {
                Object v = node.getValue();
                if (v instanceof List) {
                    List list = (List) v;
                    int x = ((Number) list.get(0)).intValue();
                    int y = ((Number) list.get(1)).intValue();
                    long time = ((Number) list.get(2)).longValue();

                    byte type = ((Number) list.get(3)).byteValue();
                    boolean circle   = (type & 0b00000001) != 0;
                    boolean slider   = (type & 0b00000010) != 0;
                    int newCombo     = (type & 0b00000100);
                    boolean spinner  = (type & 0b00001000) != 0;
                    int skip         = (type & 0b01110000);
                    boolean holdNote = (type & 0b10000000) != 0;

                    HitSound sound = new HitSound(((Number) list.get(4)).byteValue());

                    if (circle)
                        objects.add(new Circle(x, y, time, newCombo, skip, sound, parseExtras(((String) list.get(5)))));
                    else if (slider) {
                        String[] posList = ((String) list.get(5)).split("\\|");
                        Slider.SliderType sliderType = Slider.SliderType.byShortName(posList[0]);
                        List<Slider.CurvePoint> curvePointList = new ArrayList<>();
                        for (int i = 1; i < posList.length; i++) {
                            String[] xy = posList[i].split(":");
                            curvePointList.add(new Slider.CurvePoint(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
                        }
                        int repeat = ((Number) list.get(6)).intValue();
                        double pixelLength = ((Number) list.get(7)).doubleValue();

                        List<HitSound> edgeHitsounds = new ArrayList<>();
                        String[] hitList = ((String) list.get(8)).split("\\|");
                        for (String s : hitList)
                            edgeHitsounds.add(new HitSound((byte) Integer.parseInt(s)));
                        List<Pair<GeneralSection.SampleSet, GeneralSection.SampleSet>> edgeAdditions = new ArrayList<>();
                        String[] additList = ((String) list.get(9)).split("\\|");
                        for (String s : additList) {
                            String[] sa = s.split(":");
                            edgeAdditions.add(new Pair<>(GeneralSection.SampleSet.byOrdinal(Integer.parseInt(sa[0])), GeneralSection.SampleSet.byOrdinal(Integer.parseInt(sa[1]))));
                        }
                        objects.add(new Slider(x, y, time, newCombo, skip, sound, parseExtras((String) list.get(10)), sliderType, curvePointList, repeat, pixelLength, edgeHitsounds, edgeAdditions));
                    } else if (spinner || holdNote) {
                        long endTime = ((Number) list.get(5)).longValue();
                        Extras e = parseExtras((String) list.get(6));
                        if (spinner)
                            objects.add(new Spinner(x, y, time, newCombo, skip, sound, e, endTime));
                        else
                            objects.add(new HoldNote(x, y, time, newCombo, skip, sound, e, endTime));
                    }
                }
            }
        }
    }

    private Extras parseExtras(String input) {
        String[] s = input.split(":");
        if (s.length < 4 || s.length > 5) throw new OsuParseException("Extras length != 5");
        GeneralSection.SampleSet sampleSet = GeneralSection.SampleSet.byOrdinal(Integer.parseInt(s[0]));
        GeneralSection.SampleSet additionSet = GeneralSection.SampleSet.byOrdinal(Integer.parseInt(s[1]));
        int customIndex = Integer.parseInt(s[2]);
        int sampleVolume = Integer.parseInt(s[3]);
        String filename = s.length==5 ? s[4] : "";
        return new Extras(sampleSet, additionSet, customIndex, sampleVolume, filename);
    }

    public List<HitObject> getObjects() {
        return objects;
    }

    private class Extras {
        GeneralSection.SampleSet sampleSet;
        GeneralSection.SampleSet additionSet;
        int customIndex;
        int sampleVolume;
        String filename;

        public Extras(GeneralSection.SampleSet sampleSet, GeneralSection.SampleSet additionSet, int customIndex, int sampleVolume, String filename) {
            this.sampleSet = sampleSet;
            this.additionSet = additionSet;
            this.customIndex = customIndex;
            this.sampleVolume = sampleVolume;
            this.filename = filename;
        }
    }

    public static class HitSound {
        private final boolean normal;
        private final boolean whistle;
        private final boolean finish;
        private final boolean clap;

        public HitSound(byte source) {
            this.normal = (source & 0b0001) != 0;
            this.whistle = (source & 0b0010) != 0;
            this.finish = (source & 0b0100) != 0;
            this.clap = (source & 0b1000) != 0;
        }

        public boolean isNormal() {
            return normal;
        }

        public boolean isClap() {
            return clap;
        }

        public boolean isFinish() {
            return finish;
        }

        public boolean isWhistle() {
            return whistle;
        }
    }

    public abstract static class HitObject {
        int x, y;
        long time;

        int newCombo;
        int skipColours;

        HitSound sound;

        // Extras
        Extras extras;

        public HitObject(int x, int y, long time, int newCombo, int skipColours, HitSound sound, Extras extras) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.newCombo = newCombo;
            this.skipColours = skipColours;
            this.sound = sound;
            this.extras = extras;
        }
    }

    public static class Circle extends HitObject {
        public Circle(int x, int y, long time, int newCombo, int skipColours, HitSound sound, Extras extras) {
            super(x, y, time, newCombo, skipColours, sound, extras);
        }
    }

    public static class Slider extends HitObject {
        SliderType type;
        List<CurvePoint> curvePointList;
        int repeat;
        double pixelLength;
        List<HitSound> edgeHitsounds;
        List<Pair<GeneralSection.SampleSet, GeneralSection.SampleSet>> edgeAdditions;

        public static class CurvePoint {
            int x,y;

            public CurvePoint(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }

        public enum SliderType {
            LINEAR("L"), PERFECT("P"), BEZIER("B"), CATMULL("C");
            String abbrev;
            SliderType(String abbrev) {
                this.abbrev = abbrev;
            }
            static SliderType[] v = values();
            public static SliderType byShortName(String s) {
                return Arrays.stream(v).filter(sliderType -> sliderType.abbrev.equals(s)).findFirst().orElse(null);
            }
        }

        public Slider(int x, int y, long time, int newCombo, int skipColours, HitSound sound, Extras extras, SliderType type, List<CurvePoint> curvePointList, int repeat, double pixelLength, List<HitSound> edgeHitsounds, List<Pair<GeneralSection.SampleSet, GeneralSection.SampleSet>> edgeAdditions) {
            super(x, y, time, newCombo, skipColours, sound, extras);
            this.type = type;
            this.curvePointList = curvePointList;
            this.repeat = repeat;
            this.pixelLength = pixelLength;
            this.edgeHitsounds = edgeHitsounds;
            this.edgeAdditions = edgeAdditions;
        }
    }

    public static class Spinner extends HitObject {
        long endTime;

        public Spinner(int x, int y, long time, int newCombo, int skipColours, HitSound sound, Extras extras, long endTime) {
            super(x, y, time, newCombo, skipColours, sound, extras);
            this.endTime = endTime;
        }
    }

    public static class HoldNote extends Spinner {
        public HoldNote(int x, int y, long time, int newCombo, int skipColours, HitSound sound, Extras extras, long endTime) {
            super(x, y, time, newCombo, skipColours, sound, extras, endTime);
        }
    }

    public static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
