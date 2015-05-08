package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
class TableRawLineParser {

    private StringTableBuilder builder = null;

    private boolean isFirstRowHeader;
    private boolean isLastRowExcluded;
    private boolean isEmptyRowExcluded;

    private int[] fieldLengths;
    private String[] lastRecord = null;


    public TableRawLineParser(int[] fieldLengths, boolean isFirstRowHeader, boolean isLastRowExcluded, boolean isEmptyRowExcluded) {
        this.fieldLengths = fieldLengths;
        this.isFirstRowHeader = isFirstRowHeader;
        this.isLastRowExcluded = isLastRowExcluded;
        this.isEmptyRowExcluded = isEmptyRowExcluded;

        if (!isFirstRowHeader) {
            builder = getBuilderWithNumberedHeader(fieldLengths.length);
        }
    }

    public StringTableBuilder getTableBuilder() {
        finishParsing();
        return builder;
    }

    public void parseRawLine(String rawLine) {
        if (isEmptyRowExcluded && "".equals(rawLine.trim())) {
            return;
        }

        if (builder == null) {
            if (!isFirstRowHeader) {
                throw new IllegalStateException("first row should be header");
            }
            if (lastRecord != null) {
                throw new IllegalStateException("lastRecord should be null");
            }
            builder = getBuilderWithHeader(toStringArray(rawLine, fieldLengths));
        } else {
            if (lastRecord != null) {
                builder.addRecord(lastRecord);
            }
            setLastRecord(rawLine);
        }
    }

    private void finishParsing() {
        if (builder == null) {
            builder = getBuilderWithNumberedHeader(fieldLengths.length);
        }
        if (lastRecord != null && !isLastRowExcluded) {
            builder.addRecord(lastRecord);
        }
    }

    private void setLastRecord(String rawLine) {
        lastRecord = toStringArray(rawLine, fieldLengths);
    }

    private StringTableBuilder getBuilderWithNumberedHeader(int columnCount) {
        return DefaultStringTableImpl.newBuilder(StringTableFactory.getDefaultHeader(columnCount));
    }

    private StringTableBuilder getBuilderWithHeader(String[] schema) {
        return DefaultStringTableImpl.newBuilder(schema);
    }

    private static String[] toStringArray(String line, int... columnsLen) {
        String[] strArr = new String[columnsLen.length];
        int beginIndex = 0;
        for (int i = 0; i < columnsLen.length; i++) {
            int endIndex = Math.min(beginIndex + columnsLen[i], line.length());
            strArr[i] = line.substring(beginIndex, endIndex).trim();
            beginIndex = endIndex;
        }
        return strArr;
    }


}
