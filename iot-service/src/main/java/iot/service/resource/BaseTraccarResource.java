package iot.service.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.json.JsonArray;
import javax.json.JsonObject;
import iot.core.entity.BaseEntity;
import iot.core.helper.JsonConverter;
import iot.core.helper.Reflection;
import iot.core.resource.JsonResource;

/**
 *
 * @author Rafael
 * @param <T>
 * @param <I>
 */
public abstract class BaseTraccarResource<T extends BaseEntity, I extends Serializable> extends JsonResource<T, I> {

    private static final String DATA_KEY = "data";
    private static final String API_URL = "http://rastreamento.xyz/api";

    private final Class<T> clazz = Reflection.getGenericArgumentType(getClass());

    @Override
    protected String getTarget() {
        return API_URL;
    }

    public Collection<T> getEntities() {
        Collection<T> entities = new ArrayList<>();
        JsonObject jsonObject = (JsonObject) super.getJsonEntities();
        JsonArray jsonArray = jsonObject.getJsonArray(DATA_KEY);
        for (int i = 0; i < jsonArray.size(); i++) {
            T entity = JsonConverter.objectFromJson(jsonArray.getJsonObject(i), clazz);
            entities.add(entity);
        }
        return entities;
    }

    public T getEntityById(I id) {
        JsonObject jsonObject = (JsonObject) super.getJsonEntity(id);
        return JsonConverter.objectFromJson(jsonObject, clazz);
    }
}
