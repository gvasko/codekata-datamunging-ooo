package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gvasko on 2015.05.22..
 */
class DefaultStringRecordBuilderImpl implements StringRecordBuilder {

    private Map<String, String> stringMap;
    private StringRecordFactoryExt factory;

    DefaultStringRecordBuilderImpl(StringRecordFactoryExt factory) {
        stringMap = new HashMap<>();
        this.factory = factory;
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
            return factory.createStringRecord(stringMap);
        }
    }

}
