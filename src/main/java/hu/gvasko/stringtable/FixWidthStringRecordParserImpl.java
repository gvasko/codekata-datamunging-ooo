package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.10..
 */
class FixWidthStringRecordParserImpl implements StringRecordParser {

    private int[] fieldLengths;

    public FixWidthStringRecordParserImpl(int[] fieldLengths) {
        this.fieldLengths = fieldLengths;
    }

    @Override
    public int getColumnCount() {
        return fieldLengths.length;
    }

    @Override
    public String[] parseHeader(String rawLine) {
        return toUniqueStringArray(toStringArray(rawLine, fieldLengths));
    }

    @Override
    public String[] parse(String rawLine) {
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

    private static String[] toUniqueStringArray(String[] stringArray) {
        // O(n^2), but it is designed for tables with a few columns only
        String[] uniqueStringArray = new String[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            if (getCount(stringArray[i], stringArray) > 1) {
                uniqueStringArray[i] = Integer.toString(i) + stringArray[i];
            } else {
                uniqueStringArray[i] = stringArray[i];
            }
        }
        return uniqueStringArray;
    }

    private static int getCount(String ss, String[] stringArray) {
        int counter = 0;
        for (String s : stringArray) {
            if (ss.equals(s)) {
                counter++;
            }
        }
        return counter;
    }

}
