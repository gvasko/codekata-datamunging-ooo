package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableBuilder;

import java.util.*;

/**
 * Created by gvasko on 2015.05.23..
 */
class DefaultStringTableBuilderImpl implements StringTableBuilder {

    private String[] schema;
    private List<String[]> records;
    private StringTableFactoryExt tableFactory;

    DefaultStringTableBuilderImpl(StringTableFactoryExt tableFactory, String... sharedSchema) {
        Set<String> uniqueSchema = new HashSet<>(Arrays.asList(sharedSchema));
        if (uniqueSchema.size() != sharedSchema.length) {
            throw new IllegalArgumentException("Duplicated attribute in the schema");
        }
        schema = sharedSchema;
        records = new ArrayList<>();
        this.tableFactory = tableFactory;
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
        return tableFactory.createStringTable(schema, records);
    }

}
