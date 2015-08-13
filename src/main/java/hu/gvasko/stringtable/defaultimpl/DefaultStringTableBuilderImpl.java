package hu.gvasko.stringtable.defaultimpl;

import com.google.inject.Inject;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableBuilder;

import java.util.*;

/**
 * Created by gvasko on 2015.05.23..
 */
class DefaultStringTableBuilderImpl implements StringTableBuilder {

    static class ConstructorDelegateImpl implements StringTableBuilderConstructorDelegate {
        private StringTableConstructorDelegate tableCtor;

        @Inject
        public ConstructorDelegateImpl(StringTableConstructorDelegate tableCtor) {
            this.tableCtor = tableCtor;
        }

        @Override
        public StringTableBuilder call(String... schema) {
            return new DefaultStringTableBuilderImpl(tableCtor, schema);
        }

        @Override
        public StringRecordBuilderConstructorDelegate getRecordBuilderConstructor() {
            return tableCtor.getRecordBuilderConstructorDelegate();
        }

    }

    private String[] schema;
    private List<String[]> records;
    private StringTableConstructorDelegate tableCtor;

    private DefaultStringTableBuilderImpl(StringTableConstructorDelegate sharedTableCtor, String... sharedSchema) {
        Set<String> uniqueSchema = new HashSet<>(Arrays.asList(sharedSchema));
        if (uniqueSchema.size() != sharedSchema.length) {
            throw new IllegalArgumentException("Duplicated attribute in the schema");
        }
        schema = sharedSchema;
        records = new ArrayList<>();
        tableCtor = sharedTableCtor;
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
        return tableCtor.call(schema, records);
    }

}
