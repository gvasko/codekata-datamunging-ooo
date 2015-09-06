package hu.gvasko.stringtable;

import hu.gvasko.stringrecord.StringRecord;

import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.09.06..
 */
public interface CommonRecordFilters {
    Predicate<StringRecord> onlyNumbersInColumn(String column);

}
