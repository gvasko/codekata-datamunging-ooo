package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;

import java.util.Map;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringRecordImpl implements StringRecord {

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

        throw new IllegalArgumentException("Field does not exist: " +field);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(entry.getValue());
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
}
