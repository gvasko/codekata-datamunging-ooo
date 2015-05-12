package hu.gvasko.stringtable;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Created by gvasko on 2015.05.07..
 */
class DefaultStringTableImpl implements StringTable {

    private static class BuilderImpl implements StringTableBuilder {
        private String[] schema;
        private List<String[]> records;

        BuilderImpl(String... schema) {
            Set<String> uniqueSchema = new HashSet<>(Arrays.asList(schema));
            if (uniqueSchema.size() != schema.length) {
                throw new IllegalArgumentException("Duplicated attribute in the schema");
            }
            this.schema = schema;
            records = new ArrayList<>();
        }

        @Override
        public String[] getSchema() {
            return Arrays.copyOf(schema, schema.length);
        }

        @Override
        public StringTableBuilder addRecord(String... fields) {
            if (schema.length != fields.length) {
                throw new RuntimeException("Unexpected record.");
            }
            records.add(fields);
            return this;
        }

        @Override
        public StringTable build() {
            return new DefaultStringTableImpl(schema, records);
        }

    }

    static StringTableBuilder newBuilder(String... schema) {
        return new BuilderImpl(schema);
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
        return records.stream().map(this::toStringRecord).collect(Collectors.toList());
    }

    private StringRecord toStringRecord(String[] rec) {
        StringRecordBuilder recBuilder = DefaultStringRecordImpl.newBuilder();
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
    public void addStringDecoderToColumns(UnaryOperator<String> decoder, String... fields) {
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
