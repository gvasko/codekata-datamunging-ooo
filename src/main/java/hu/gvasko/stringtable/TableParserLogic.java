package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.10..
 */
interface TableParserLogic {
    StringTable getTable();

    void parseRawLine(String rawLine);
}
