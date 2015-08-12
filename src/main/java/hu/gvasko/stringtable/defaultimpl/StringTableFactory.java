package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecordBuilderConstructor;
import hu.gvasko.stringtable.StringTable;

import java.util.List;

/**
 * Created by gvasko on 2015.05.23..
 */
interface StringTableFactory {
    StringTable createNew(String[] sharedSchema, List<String[]> sharedRecords);
    StringRecordBuilderConstructor getRecordBuilderConstructorDelegate();
}
