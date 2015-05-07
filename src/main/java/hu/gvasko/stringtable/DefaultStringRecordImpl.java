package hu.gvasko.stringtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringRecordImpl implements  StringRecord {

    static class Builder {

        List<String> order;
        Map<String, String> stringMap;

        Builder() {
            order = new ArrayList<>();
            stringMap = new HashMap<>();
        }

        Builder addField(String key, String value) {
            order.add(key);
            stringMap.put(key, value);
            return this;
        }

        DefaultStringRecordImpl build() {
            if (stringMap.isEmpty()) {
                throw new IllegalStateException("Empty record cannot be created.");
            } else {
                return new DefaultStringRecordImpl(order, stringMap);
            }
        }
    }

    static Builder newBuilder() {
        return new Builder();
    }

    private List<String> fieldOrder;
    private Map<String, String> fields;

    DefaultStringRecordImpl(List<String> sharedFieldOrder, Map<String, String> sharedFields) {
        for (String field : sharedFieldOrder) {
            if (!sharedFields.containsKey(field)) {
                throw new IllegalArgumentException("Invalid fieldOrder: " + field);
            }
        }
        fieldOrder = sharedFieldOrder;
        fields = sharedFields;
    }

    public String get(String field) {
        if (field == null) {
            throw new NullPointerException();
        }

        if (fields.containsKey(field)) {
            return fields.get(field);
        }

        throw new RuntimeException("Field does not exist.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String field : fieldOrder) {
            if (!first) {
                sb.append(',');
            }
            sb.append(fields.get(field));
            first = false;
        }
        return sb.toString();
    }
}
