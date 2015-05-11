package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.11..
 */
interface StringRecordBuilder {
    StringRecordBuilder addField(String key, String value);

    StringRecord build();
}
