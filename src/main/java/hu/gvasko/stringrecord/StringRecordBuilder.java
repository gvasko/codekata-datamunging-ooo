package hu.gvasko.stringrecord;

/**
 * Created by Gvasko on 2015.05.11..
 */
public interface StringRecordBuilder {
    StringRecordBuilder addField(String key, String value);
    StringRecordBuilder addFields(String[] schema, String[] values);

    StringRecord build();
}
