package hu.gvasko.stringtable;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.06..
 */
public interface StringTable {
    int getRowCount();

    StringRecord getRecord(int row) throws IndexOutOfBoundsException;

    List<StringRecord> getAllRecords();

    // how to process each field in the selected range
    void addStringDecoder(UnaryOperator<String> decoder, String... fields);
}
