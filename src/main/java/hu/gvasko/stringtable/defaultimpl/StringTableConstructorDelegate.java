package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;
import hu.gvasko.stringtable.StringTable;

import java.util.List;

/**
 * Created by gvasko on 2015.05.23..
 */
interface StringTableConstructorDelegate {
    StringTable call(String[] sharedSchema, List<String[]> sharedRecords);
    StringRecordBuilderConstructorDelegate getRecordBuilderConstructorDelegate();
}
