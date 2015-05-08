package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
interface StringTableBuilder {
    StringTableBuilder addRecord(String... fields);
    StringTable build();
}
