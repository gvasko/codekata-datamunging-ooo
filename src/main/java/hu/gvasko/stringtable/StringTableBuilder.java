package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
public interface StringTableBuilder {
    String[] getSchema();
    StringTableBuilder addRecord(String... fields);
    StringTable build();
}
