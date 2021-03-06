package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.stringtable.StringTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Created by gvasko on 2015.05.07..
 */
class DefaultStringTableImpl implements StringTable {

    private String[] schema;
    private List<String[]> records;
    private Map<String,Function<String,String>> fieldDecoders;
    private StringRecordFactory recordFactory;

    DefaultStringTableImpl(StringRecordFactory recordFactory, String[] sharedSchema, List<String[]> sharedRecords) {
        for (int i = 0; i < sharedRecords.size(); i++) {
            String[] rec = sharedRecords.get(i);
            if (sharedSchema.length != rec.length) {
                throw new IllegalArgumentException("Record #" + Integer.toString(i) + " [" + String.join(",", rec) + "] does not fulfill schema [" + String.join(",", sharedSchema) + "]");
            }
        }
        schema = sharedSchema;
        records = sharedRecords;
        fieldDecoders = new HashMap<>();
        this.recordFactory = recordFactory;
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
        StringRecordBuilder recBuilder = recordFactory.createStringRecordBuilder();
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
