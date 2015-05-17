package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringRecordImpl implements  StringRecord {

    private static class BuilderImpl implements StringRecordBuilder {

        Map<String, String> stringMap;

        @Inject
        BuilderImpl() {
            stringMap = new HashMap<>();
        }

        @Override
        public BuilderImpl addField(String key, String value) {
            stringMap.put(key, value);
            return this;
        }

        @Override
        public StringRecord build() {
            if (stringMap.isEmpty()) {
                throw new IllegalStateException("Empty record cannot be created.");
            } else {
                return new DefaultStringRecordImpl(stringMap);
            }
        }
    }

//    static StringRecordBuilder newBuilder() {
//        return new BuilderImpl();
//    }

    static class BuilderFactoryImpl implements StringRecordBuilderFactory {
        @Override
        public StringRecordBuilder createNew() {
            return new BuilderImpl();
        }
    }

    static StringRecord newRecord(String[] schema, String[] values) {
        if (schema.length != values.length) {
            throw new IllegalArgumentException("Invalid values according to the schema.");
        }
        if (schema.length == 0) {
            throw new IllegalStateException("Empty record cannot be created.");
        }

        Map<String, String> stringMap = new HashMap<>();
        for (int i = 0; i < schema.length; i++) {
            stringMap.put(schema[i], values[i]);
        }

        return new DefaultStringRecordImpl(stringMap);
    }

    private List<String> fieldOrder;
    private Map<String, String> fields;

    DefaultStringRecordImpl(Map<String, String> sharedFields) {
        fields = sharedFields;
    }

    @Override
    public String get(String field) {
        if (field == null) {
            throw new NullPointerException();
        }

        if (fields.containsKey(field)) {
            return fields.get(field);
        }

        throw new RuntimeException("Field does not exist: " +field);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (String field : fields.keySet()) {
            if (!first) {
                sb.append(',');
            }
            sb.append(fields.get(field));
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
}
