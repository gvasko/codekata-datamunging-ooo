package hu.gvasko.stringtable.defaultimpl;

import com.google.inject.Inject;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableBuilder;

import java.util.*;

/**
 * Created by gvasko on 2015.05.23..
 */
class DefaultStringTableBuilderImpl implements StringTableBuilder {

    static class FactoryImpl implements StringTableBuilderFactory {
        private StringTableFactory tableFactory;

        @Inject
        public FactoryImpl(StringTableFactory tableFactory) {
            this.tableFactory = tableFactory;
        }

        @Override
        public StringTableBuilder createNewTableBuilder(String... schema) {
            return new DefaultStringTableBuilderImpl(tableFactory, schema);
        }

        @Override
        public StringRecordBuilder createNewRecordBuilder() {
            return tableFactory.getRecordBuilderFactory().createNew();
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
