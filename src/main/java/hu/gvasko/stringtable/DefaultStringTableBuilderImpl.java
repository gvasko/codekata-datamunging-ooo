package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.*;

/**
 * Created by gvasko on 2015.05.23..
 */
class DefaultStringTableBuilderImpl implements StringTableBuilder {

    static class FactoryImpl implements StringTableBuilderFactory {
        private StringTableFactory sRecBuilderFactory;

        @Inject
        public FactoryImpl(StringTableFactory sRecBuilderFactory) {
            this.sRecBuilderFactory = sRecBuilderFactory;
        }

        @Override
        public StringTableBuilder createNew(String... schema) {
            return new DefaultStringTableBuilderImpl(sRecBuilderFactory, schema);
        }

        @Override
        public StringTableFactory getTableFactory() {
            return sRecBuilderFactory;
        }
    }

    private String[] schema;
    private List<String[]> records;
    private StringTableFactory tableFactory;

    private DefaultStringTableBuilderImpl(StringTableFactory sharedTableFactory, String... sharedSchema) {
        Set<String> uniqueSchema = new HashSet<>(Arrays.asList(sharedSchema));
        if (uniqueSchema.size() != sharedSchema.length) {
            throw new IllegalArgumentException("Duplicated attribute in the schema");
        }
        schema = sharedSchema;
        records = new ArrayList<>();
        tableFactory = sharedTableFactory;
    }

    @Override
    public String[] getSchema() {
        return Arrays.copyOf(schema, schema.length);
    }

    @Override
    public StringTableBuilder addRecord(String... fields) {
        if (schema.length != fields.length) {
            throw new IllegalArgumentException("Unexpected record.");
        }
        records.add(fields);
        return this;
    }

    @Override
    public StringTable build() {
        return tableFactory.createNew(schema, records);
    }

}
