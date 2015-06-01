package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.StringTable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.10..
 */
interface TableParserLogic {
    StringTable getTable();

    boolean isFirstRowHeader();
    void setFirstRowHeader(boolean f);

    void setLineFilters(List<Predicate<String>> lineFilters);
    void addLineFilter(Predicate<String> lineFilter);
    void setRecordFilters(List<Predicate<StringRecord>> recordFilters);
    void addRecordFilter(Predicate<StringRecord> recordFilter);

    void parseRawLine(String rawLine);
}
