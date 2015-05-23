package hu.gvasko.stringtable;

import java.util.List;

/**
 * Created by gvasko on 2015.05.23..
 */
interface StringTableFactory {
    StringTable createNew(String[] sharedSchema, List<String[]> sharedRecords);
    StringRecordBuilderFactory getRecordBuilderFactory();
}
