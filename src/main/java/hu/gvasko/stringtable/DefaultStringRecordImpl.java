package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.List;
import java.util.Map;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringRecordImpl implements  StringRecord {

    static class FactoryImpl implements StringRecordFactory {

        @Inject
        public FactoryImpl() {

        }

        @Override
        public StringRecord createNew(Map<String, String> stringMap) {
            return new DefaultStringRecordImpl(stringMap);
        }
    }

    private List<String> fieldOrder;
    private Map<String, String> fields;

    private DefaultStringRecordImpl(Map<String, String> sharedFields) {
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

        throw new IllegalArgumentException("Field does not exist: " +field);
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
            sb.append(field);
            sb.append(':');
            sb.append(fields.get(field));
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
}
