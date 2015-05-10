package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.10..
 */
interface StringRecordParser {
    int getColumnCount();
    String[] parseHeader(String rawLine);
    String[] parse(String rawLine);
}
