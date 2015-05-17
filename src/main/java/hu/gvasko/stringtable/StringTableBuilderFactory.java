package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringTableBuilderFactory {
    StringTableBuilder createNew(String... schema);
    StringRecordBuilderFactory getRecordBuilderFactory();
}
