package me.zeroeightysix.osureader.section;

import me.zeroeightysix.osureader.node.*;
import me.zeroeightysix.osureader.parse.OsuParseException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by 086 on 16/05/2018.
 */
class Section {

    Section(OsuSection source) throws IllegalAccessException {
        Set<Field> fields = Arrays.stream(getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Source.class)).collect(Collectors.toSet());
        for (Field field : fields) {
            Source s = field.getAnnotation(Source.class);
            OsuNode r = source.getEntries().stream().filter(osuNode -> osuNode instanceof OsuMappedNode).filter(osuNode -> ((OsuMappedNode) osuNode).getKey().equals(s.value())).findFirst().orElse(null);
            if (r == null)
//                throw new OsuParseException(s.value() + " is not present");
                continue;
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            if (field.getType() == boolean.class && r instanceof OsuNumberNode)
                field.set(this, ((OsuNumberNode) r).getValueDouble() != 0);
            else if (field.getType() == int.class && r instanceof OsuNumberNode)
                field.set(this, ((OsuNumberNode) r).getValue().intValue());
            else if (field.getType() == double.class && r instanceof OsuNumberNode)
                field.set(this, ((OsuNumberNode) r).getValueDouble());
            else if (field.getType() == String.class && r instanceof OsuStringNode)
                field.set(this, ((OsuStringNode) r).getValue());
            else if (field.getType().isEnum()) {
                if (r instanceof OsuNumberNode) {
                    int iVal = ((OsuNumberNode) r).getValue().intValue();
                    try {
                        field.set(this, field.getType().getEnumConstants()[iVal]);
                    }catch (Exception e) {
                        throw new OsuParseException(String.format("%d is out of range for enum %s", iVal, s.value()));
                    }
                } else if (r instanceof OsuStringNode) {
                    String filtered = ((OsuStringNode) r).getValue().replaceAll("\\W", "");
                    for (Object o : field.getType().getEnumConstants()) {
                        if (((Enum) o).name().equalsIgnoreCase(filtered)) {
                            field.set(this, o);
                        }
                    }
                }
            } else if (field.getType() == List.class) {
                if (r instanceof OsuListNode) {
                    field.set(this, ((OsuListNode) r).getValue());
                }else{
                    List list = new ArrayList();
                    list.add(r.getValue());
                    field.set(this, list);
                }
            } else{
                field.setAccessible(accessible);
                //throw new OsuParseException("Unknown node type " + r.getClass().getSimpleName() + " for field " + field.getName() + " of type " + field.getType().getSimpleName());
            }
            field.setAccessible(accessible);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    protected @interface Source {
        String value();
    }

}
