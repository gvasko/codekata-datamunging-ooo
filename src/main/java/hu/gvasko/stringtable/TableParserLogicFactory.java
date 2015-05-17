package hu.gvasko.stringtable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.17..
 */
interface TableParserLogicFactory {
    TableParserLogic createNew(int[] sharedFieldLengths, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters);
}
