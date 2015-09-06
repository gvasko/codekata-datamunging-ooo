package hu.gvasko.stringtable;

import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.09.06..
 */
public interface CommonLineFilters {
    Predicate<String> skipEmptyLines();
    Predicate<String> skipSplitterLines();
}

