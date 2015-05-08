package hu.gvasko.stringtable;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.07..
 */
class DefaultStringTableImpl implements StringTable {

    static class Builder {
        private String[] schema;
        private List<String[]> records;

        Builder(String... schema) {
            // TODO: no duplicate names allowed
            this.schema = schema;
            records = new ArrayList<>();
        }

        Builder addRecord(String... fields) {
            if (schema.length != fields.length) {
                throw new RuntimeException("Unexpected record.");
            }
            records.add(fields);
            return this;
        }

        DefaultStringTableImpl build() {
            return new DefaultStringTableImpl(schema, records);
        }

    }

    static Builder newBuilder(String... schema) {
        return new Builder(schema);
    }

    private String[] schema;
    private List<String[]> records;
    private Map<String,Function<String,String>> fieldDecoders;

    DefaultStringTableImpl(String[] sharedSchema, List<String[]> sharedRecords) {
        schema = sharedSchema;
        records = sharedRecords;
        fieldDecoders = new HashMap<>();
    }

    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public StringRecord getRecord(int row) throws IndexOutOfBoundsException {
        return toStringRecord(records.get(row));
    }

    @Override
    public List<StringRecord> getAllRecords() {
        List<StringRecord> resultRecords = new ArrayList<>();
        for (String[] rec : records) {
            resultRecords.add(toStringRecord(rec));
        }
        return resultRecords;
    }

    private StringRecord toStringRecord(String[] rec) {
        DefaultStringRecordImpl.Builder recBuilder = DefaultStringRecordImpl.newBuilder();
        for (int i = 0; i < schema.length; i++) {
            String field = schema[i];
            String value = getDecodedValue(field, rec[i]);
            recBuilder.addField(field, value);
        }
        return recBuilder.build();
    }

    private String getDecodedValue(String field, String originalValue) {
        if (!fieldDecoders.containsKey(field)) {
            return originalValue;
        }
        return fieldDecoders.get(field).apply(originalValue);
    }

    @Override
    public void addStringDecoder(UnaryOperator<String> decoder, String... fields) {
        if (decoder == null) {
            throw new NullPointerException("Decoder is null.");
        }
        if (fields.length == 0) {
            throw new IllegalArgumentException("No fields provided.");
        }
        for (String field : fields) {
            if (!Arrays.asList(schema).contains(field)) {
                throw new IllegalArgumentException("Invalid field provided: " + field);
            }
        }
        for (String field : fields) {
            // TODO: What if null is returned?
            if (fieldDecoders.containsKey(field)) {
                fieldDecoders.put(field, fieldDecoders.get(field).andThen(decoder));
            } else {
                fieldDecoders.put(field, decoder);
            }
        }
    }
}
