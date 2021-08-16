package me.zeroeightysix.osureader.parse;

import me.zeroeightysix.osureader.OsuFile;
import me.zeroeightysix.osureader.OsuReader;
import me.zeroeightysix.osureader.node.*;
import me.zeroeightysix.osureader.section.OsuSection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
public class StandardOsuFileParser implements OsuFileParser {

    private final OsuNode NULL_NODE = () -> null;
    private final static Pattern ENTRY_PATTERN = Pattern.compile("^[\\w, ]+:.*");
    private final static Pattern COMMENT_PATTERN = Pattern.compile("\\/\\/.*");

    private OsuNode parseNode(final String line) {
        final String trimmed = line.trim();
        if (trimmed.isEmpty()) return NULL_NODE;

        if (trimmed.startsWith(OsuVersionNode.PREFIX)) {
            try {
                return new OsuVersionNode(Integer.parseInt(trimmed.substring(OsuVersionNode.PREFIX.length())));
            }catch (NumberFormatException e) {
                throw new OsuParseException("File format version is nonnumerical", e);
            }
        }
        if (trimmed.startsWith("[") && trimmed.endsWith("]"))
            return new OsuSectionNode(trimmed.substring(1,trimmed.length()-1));

        if (COMMENT_PATTERN.matcher(line).matches()) return new OsuComment(line.substring(2));

        Matcher entryMatcher = ENTRY_PATTERN.matcher(trimmed);
        if (entryMatcher.matches() && trimmed.split(":", -1).length==2) {
            int splitchar = trimmed.indexOf(':');
            String key = trimmed.substring(0,splitchar).trim();
            String value = trimmed.substring(splitchar+1).trim();
            try {
                return new OsuNumberNode(key, Double.parseDouble(value.trim()));
            }catch (NumberFormatException e) {
                List o = Arrays.stream(value.split(",")).map(s -> {
                    try {
                        return Double.parseDouble(s);
                    }catch (NumberFormatException e1) {
                        return s;
                    }
                }).collect(Collectors.toList());
                if (o.size() == 3 && o.stream().allMatch(o1 -> o1 instanceof Number)) {
                    return new OsuColourNode(key, new OsuReader.Colour(((Double) o.get(0)).intValue(),((Double) o.get(1)).intValue(),((Double) o.get(2)).intValue()));
                }
                if (o.size() == 1) {
                    return new OsuStringNode(key, value);
                }else{
                    return new OsuListNode(key, o);
                }
            }
        }

        try {
            return new OsuPrimitiveNode(Double.parseDouble(trimmed));
        }catch (NumberFormatException e) {
            if (trimmed.contains(",")) {
                return new OsuPrimitiveNode(Arrays.stream(trimmed.split(",")).map(s -> {
                    try {
                        return Double.parseDouble(s);
                    }catch (NumberFormatException e1) {
                        return s;
                    }
                }).collect(Collectors.toList()));
            }
        }

        return new OsuPrimitiveNode(trimmed);
    }

    private OsuFile fromNodes(Collection<OsuNode> nodes) {
        int version = -1;
        List<OsuSection> sections = new ArrayList<>();

        OsuSection section = null;
        for (OsuNode node : nodes) {
            if (node == NULL_NODE) continue;
            if (node instanceof OsuSectionNode) {
                if (section != null) sections.add(section);
                section = new OsuSection(((OsuSectionNode) node).getValue());
            } else if (node instanceof OsuVersionNode) {
                version = ((OsuVersionNode) node).getVersion();
            } else if (section == null) {
                throw new OsuParseException("Floating node outside of section");
            } else
                section.add(node);
        }
        sections.add(section);

        return new OsuFile(version, sections);
    }

    @Override
    public OsuFile parse(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<OsuNode> nodes = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null)
            nodes.add(parseNode(line));
        reader.close();
        return fromNodes(nodes);
    }

    @Override
    public OsuFile parse(Path file) throws IOException {
        Collection<String> lines = Files.readAllLines(file);
        return fromNodes(lines.stream().map(this::parseNode).collect(Collectors.toList()));
    }
}
