package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableFactory;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.09.06..
 */
interface StringTableFactoryExt extends StringTableFactory {
    StringRecordFactory getRecordFactory();
    StringTable createStringTable(String[] sharedSchema, List<String[]> sharedRecords);
    TableParserLogic createTableParserLogic(StringRecordParser sharedRecParser);
    TableParserLogic createTableParserLogic(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters);
}
