package hu.gvasko.stringrecord.defaultimpl;

import com.google.inject.Inject;
import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordBuilderConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gvasko on 2015.05.22..
 */
class DefaultStringRecordBuilderImpl implements StringRecordBuilder {

    static class ConstructorImpl implements StringRecordBuilderConstructor {

        private StringRecordConstructorDelegate recordFactory;

        @Inject
        public ConstructorImpl(StringRecordConstructorDelegate recordFactory) {
            this.recordFactory = recordFactory;
        }

        @Override
        public StringRecordBuilder call() {
            return new DefaultStringRecordBuilderImpl(recordFactory);
        }
    }

    private Map<String, String> stringMap;
    private StringRecordConstructorDelegate stringRecordCtor;

    private DefaultStringRecordBuilderImpl(StringRecordConstructorDelegate sharedRecordCtor) {
        stringMap = new HashMap<>();
        stringRecordCtor = sharedRecordCtor;
    }

    @Override
    public DefaultStringRecordBuilderImpl addField(String key, String value) {
        if (stringMap.containsKey(key)) {
            String alreadyAddedValue = stringMap.get(key);
            throw new IllegalArgumentException("Value " + value + " cannot be added to field " + key + " because already added: " + alreadyAddedValue);
        }
        stringMap.put(key, value);
        return this;
    }

    @Override
    public StringRecordBuilder addFields(String[] schema, String[] values) {
        if (schema.length != values.length) {
            throw new IllegalArgumentException("Invalid values according to the schema.");
        }

        for (int i = 0; i < schema.length; i++) {
            addField(schema[i], values[i]);
        }
        return this;
    }

    @Override
    public StringRecord build() {
        if (stringMap.isEmpty()) {
            throw new IllegalStateException("Empty record cannot be created.");
        } else {
            return stringRecordCtor.call(stringMap);
        }
    }

}
