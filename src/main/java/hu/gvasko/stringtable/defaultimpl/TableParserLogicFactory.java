package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.StringRecordParser;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.17..
 */
interface TableParserLogicFactory {
    TableParserLogic createNew(StringRecordParser sharedRecParser);
    TableParserLogic createNew(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters);
}
