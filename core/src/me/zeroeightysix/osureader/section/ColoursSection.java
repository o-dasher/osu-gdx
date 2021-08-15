package me.zeroeightysix.osureader.section;

import me.zeroeightysix.osureader.OsuReader;
import me.zeroeightysix.osureader.node.OsuColourNode;
import me.zeroeightysix.osureader.node.OsuNode;
import me.zeroeightysix.osureader.parse.OsuParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 086 on 16/05/2018.
 */
public class ColoursSection extends Section {

    private OsuReader.Colour[] comboColours;
    @Source("SliderBody") private OsuReader.Colour sliderBody;
    @Source("SliderTrackOverride") private OsuReader.Colour sliderTrackOverride;
    @Source("SliderBorder") private OsuReader.Colour sliderBorder;

    public ColoursSection(OsuSection source) throws IllegalAccessException {
        super(source);

        int highest = 0;
        HashMap<Integer, OsuColourNode> nodes = new HashMap<>();
        for (OsuNode node : source.getEntries()) {
            if (node instanceof OsuColourNode) {
                String name = ((OsuColourNode) node).getKey();
                if (name.startsWith("Combo")) {
                    try {
                        int n = Integer.parseInt(name.substring(5))-1;
                        nodes.put(n, (OsuColourNode) node);
                        highest = Math.max(n, highest);
                    }catch (NumberFormatException e) {
                        throw new OsuParseException("Combo index is nonnumerical", e);
                    }
                }
            }
        }
        comboColours = new OsuReader.Colour[highest+1];
        for (Map.Entry<Integer, OsuColourNode> entry : nodes.entrySet()) comboColours[entry.getKey()] = entry.getValue().getValue();
    }

    public OsuReader.Colour getSliderBody() {
        return sliderBody;
    }

    public OsuReader.Colour getSliderBorder() {
        return sliderBorder;
    }

    public OsuReader.Colour getSliderTrackOverride() {
        return sliderTrackOverride;
    }

    public OsuReader.Colour[] getComboColours() {
        return comboColours;
    }
}
