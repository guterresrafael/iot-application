package iot.core.helper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Guterres
 */
public final class Assembler implements Serializable {

    private static final long serialVersionUID = -2126578828818584021L;

    private Assembler() {
    }

    public static void assembler(Object objectFrom, Object objectTo) {
        try {
            Collection<Field> fieldsFrom = new ArrayList<>();
            Collection<Field> fieldsTo = new ArrayList<>();
            Reflection.getAllFields(fieldsFrom, objectFrom.getClass());
            Reflection.getAllFields(fieldsTo, objectTo.getClass());
            for (Field fieldTo : fieldsTo) {
                for (Field fieldFrom : fieldsFrom) {
                    if (fieldTo.getName().equals(fieldFrom.getName())) {
                        fieldTo.setAccessible(true);
                        fieldFrom.setAccessible(true);
                        if (fieldTo.getType() == fieldFrom.getType()) {
                            fieldTo.set(objectTo, fieldFrom.get(objectFrom));
                        } else {
                            //TODO: Implementar annotation para quando forem types diferentes
                        }
                    } else {
                        //TODO: Implementar annotation para quando forem nomes diferentes
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
    }

    public static void assembler(Map<String, String> mapFrom, Object objectTo) {
        try {
            Collection<Field> fieldsTo = new ArrayList<>();
            Reflection.getAllFields(fieldsTo, objectTo.getClass());
            for (Field fieldTo : fieldsTo) {
                for (Map.Entry<String, String> fieldFrom : mapFrom.entrySet()) {
                    if (fieldTo.getName().equals(fieldFrom.getKey())) {
                        fieldTo.setAccessible(true);
                        if (fieldTo.getType() == String.class) {
                            fieldTo.set(objectTo, fieldFrom.getValue());
                        } else if (fieldTo.getType() == Integer.class) {
                            fieldTo.set(objectTo, Integer.parseInt(fieldFrom.getValue()));
                        } else {
                            //TODO: Implementar annotation para quando forem types diferentes
                        }
                    } else {
                        //TODO: Implementar annotation para quando forem nomes diferentes
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
    }
}
