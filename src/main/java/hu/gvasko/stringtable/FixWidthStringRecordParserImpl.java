package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.10..
 */
class FixWidthStringRecordParserImpl implements StringRecordParser {

    private int[] fieldLengths;

    FixWidthStringRecordParserImpl(int[] fieldLengths) {
        this.fieldLengths = fieldLengths;
    }

    @Override
    public int getColumnCount() {
        return fieldLengths.length;
    }

    @Override
    public String[] parseRecord(String rawLine) {
        return toStringArray(rawLine, fieldLengths);
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
