package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.10..
 */
public interface StringRecordParser {
    int getColumnCount();
    String[] parseHeader(String rawLine);
    String[] parseRecord(String rawLine);
}
