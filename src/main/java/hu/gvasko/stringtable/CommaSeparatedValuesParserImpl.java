package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.22..
 */
class CommaSeparatedValuesParserImpl implements StringRecordParser {

    private final int columnCount;
    private static final String commaRegex = "\\,";

    public CommaSeparatedValuesParserImpl(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String[] parseRecord(String rawLine) {
        String[] values = rawLine.split(commaRegex);

        if (columnCount > values.length) {
            throw new IllegalArgumentException("Minimal number of columns is " + columnCount + ". Raw line contains different number of columns: " + rawLine);
        }

        String[] returnValues = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            returnValues[i] = values[i].trim();
        }
        return returnValues;
    }
}
