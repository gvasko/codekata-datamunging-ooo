package hu.gvasko.stringtable;

import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.06..
 */
public interface StringTableParser extends AutoCloseable {
    StringTable parse(int... fieldLengths);
    StringTableParser firstRowIsHeader();
    void addLineFilter(Predicate<String> lineFilter);
    void addRecordFilter(Predicate<StringRecord> recordFilter);
}
