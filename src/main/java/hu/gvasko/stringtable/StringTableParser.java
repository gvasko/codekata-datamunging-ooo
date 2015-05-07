package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.06..
 */
public interface StringTableParser extends AutoCloseable {
    StringTable parse(int... fieldLengths);

    // Select the range to be parsed
    StringTableParser firstRowIsHeader();
    StringTableParser excludeLastRow();
    StringTableParser excludeEmptyRows();
}
