package hu.gvasko.bootstrap.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gvasko on 2015.09.07..
 */
class Registry {
    private Map<Class<?>, Object> items = new HashMap<Class<?>, Object>();

    public <T> void putItem(Class<T> type, T instance) {
        if (type == null) {
            throw new NullPointerException("Type is null");
        }
        items.put(type, type.cast(instance));
    }

    public <T> T getItem(Class<T> type) {
        return type.cast(items.get(type));
    }

    public <T> boolean contains(Class<T> type) {
        return items.containsKey(type);
    }

    public <T> T removeItem(Class<T> type) {
        return type.cast(items.remove(type));
    }
}
