package hu.gvasko.stringtable.recordparsers;

import hu.gvasko.stringtable.StringRecordParser;

/**
 * Created by gvasko on 2015.10.06..
 */
public class SpaceSeparatedTextParserImpl implements StringRecordParser {

    private final int columnCount;

    public SpaceSeparatedTextParserImpl(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String[] parseRecord(String rawLine) {
        String safeLine = rawLine.trim();
        String[] values = safeLine.split("\\s+");

        String[] returnValues = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            if (i < values.length) {
                returnValues[i] = values[i].trim();
            } else {
                returnValues[i] = "";
            }
        }
        return returnValues;
    }
}
