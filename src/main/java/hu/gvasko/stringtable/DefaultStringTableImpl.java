package hu.gvasko.stringtable;

import com.google.inject.Inject;

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
        private StringRecordBuilderFactory sRecBuilderFactory;

        BuilderImpl(StringRecordBuilderFactory sharedSRecBuilderFactory, String... sharedSchema) {
            Set<String> uniqueSchema = new HashSet<>(Arrays.asList(sharedSchema));
            if (uniqueSchema.size() != sharedSchema.length) {
                throw new IllegalArgumentException("Duplicated attribute in the schema");
            }
            schema = sharedSchema;
            records = new ArrayList<>();
            sRecBuilderFactory = sharedSRecBuilderFactory;
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
            return new DefaultStringTableImpl(sRecBuilderFactory, schema, records);
        }

    }

    // TODO: delete method
//    static StringTableBuilder newBuilder(StringRecordBuilderFactory sharedSRecBuilderFactory, String... schema) {
//        return new BuilderImpl(sharedSRecBuilderFactory, schema);
//    }

    static class BuilderFactoryImpl implements StringTableBuilderFactory {
        private StringRecordBuilderFactory sRecBuilderFactory;

        @Inject
        public BuilderFactoryImpl(StringRecordBuilderFactory sRecBuilderFactory) {
            this.sRecBuilderFactory = sRecBuilderFactory;
        }

        @Override
        public StringTableBuilder createNew(String... schema) {
            return new BuilderImpl(sRecBuilderFactory, schema);
        }
    }

    private String[] schema;
    private List<String[]> records;
    private Map<String,Function<String,String>> fieldDecoders;
    private StringRecordBuilderFactory sRecBuilderFactory;

    DefaultStringTableImpl(StringRecordBuilderFactory sharedSRecBuilderFactory, String[] sharedSchema, List<String[]> sharedRecords) {
        schema = sharedSchema;
        records = sharedRecords;
        fieldDecoders = new HashMap<>();
        sRecBuilderFactory = sharedSRecBuilderFactory;
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
        StringRecordBuilder recBuilder = sRecBuilderFactory.createNew();
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
