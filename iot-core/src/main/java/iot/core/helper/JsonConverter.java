package iot.core.helper;

import java.beans.Introspector;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import iot.core.annotation.JsonIgnore;

/**
 *
 * @author Rafael
 */
public final class JsonConverter {

    private JsonConverter() {
    }

    //private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTime();

    public static Date parseDate(String value) {
        //return DATE_FORMAT.parseDateTime(value).toDate();
        return null;
    }

    public static <T> T objectFromJson(Reader reader, Class<T> type) throws ParseException {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            return objectFromJson(jsonReader.readObject(), type);
        }
    }

    public static <T> T objectFromJson(JsonObject json, Class<T> type) {
        T object = Reflection.instantiateClass(type);

        Method[] methods = object.getClass().getMethods();

        for (final Method method : methods) {
            if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {

                final String name = Introspector.decapitalize(method.getName().substring(3));
                Class<?> parameterType = method.getParameterTypes()[0];

                if (json.containsKey(name)) {
                    try {
                        if (parameterType.equals(boolean.class)
                            || parameterType.equals(Boolean.class)) {
                            method.invoke(object, json.getBoolean(name));
                        } else if (parameterType.equals(int.class)
                                   || parameterType.equals(Integer.class)) {
                            method.invoke(object, json.getJsonNumber(name).intValue());
                        } else if (parameterType.equals(long.class)
                                   || parameterType.equals(Long.class)) {
                            if (json.get(name).getValueType() == JsonValue.ValueType.NUMBER) {
                                method.invoke(object, json.getJsonNumber(name).longValue());
                            }
                        } else if (parameterType.equals(double.class)
                                   || parameterType.equals(Double.class)) {
                            method.invoke(object, json.getJsonNumber(name).doubleValue());
                        } else if (parameterType.equals(String.class)) {
                            method.invoke(object, json.getString(name));
                        } else if (parameterType.equals(Date.class)) {
                            //method.invoke(object, DATE_FORMAT.parseDateTime(json.getString(name)).toDate());
                        } else if (parameterType.equals(Map.class)) {
                            //method.invoke(object, MiscFormatter.fromJson(json.getJsonObject(name)));
                        }
                    } catch (IllegalAccessException | InvocationTargetException error) {
                        //Log.warning(error);
                    }
                }
            }
        }

        return object;
    }

    public static <T> JsonObject objectToJson(T object) {

        JsonObjectBuilder json = Json.createObjectBuilder();

        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                String name = Introspector.decapitalize(method.getName().substring(3));
                try {
                    if (method.getReturnType().equals(boolean.class)) {
                        json.add(name, (Boolean) method.invoke(object));
                    } else if (method.getReturnType().equals(int.class)) {
                        json.add(name, (Integer) method.invoke(object));
                    } else if (method.getReturnType().equals(long.class)) {
                        json.add(name, (Long) method.invoke(object));
                    } else if (method.getReturnType().equals(double.class)) {
                        json.add(name, (Double) method.invoke(object));
                    } else if (method.getReturnType().equals(String.class)) {
                        String value = (String) method.invoke(object);
                        if (value != null) {
                            json.add(name, value);
                        }
                    } else if (method.getReturnType().equals(Date.class)) {
                        Date value = (Date) method.invoke(object);
                        if (value != null) {
                            //json.add(name, DATE_FORMAT.print(new DateTime(value)));
                        }
                    } else if (method.getReturnType().equals(Map.class)) {
                        //json.add(name, MiscFormatter.toJson((Map) method.invoke(object)));
                    }
                } catch (IllegalAccessException | InvocationTargetException error) {
                    //Log.warning(error);
                }
            }
        }

        return json.build();
    }

    public static JsonArray arrayToJson(Collection<?> array) {

        JsonArrayBuilder json = Json.createArrayBuilder();

        for (Object object : array) {
            json.add(objectToJson(object));
        }

        return json.build();
    }
}
