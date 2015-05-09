package hu.gvasko.stringtable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Gvasko on 2015.05.08..
 */
class TableRawLineParser {

    private StringTableBuilder builder = null;

    private boolean isFirstRowHeader;

    private int[] fieldLengths;
    private String[] lastRecord = null;
    private List<Predicate<String>> linePredicates;
    private List<Predicate<StringRecord>> recordPredicates;

    public TableRawLineParser(
            int[] fieldLengths,
            boolean isFirstRowHeader,
            List<Predicate<String>> linePredicates,
            List<Predicate<StringRecord>> recordPredicates) {
        this.fieldLengths = fieldLengths;
        this.isFirstRowHeader = isFirstRowHeader;
        this.linePredicates = linePredicates;
        this.recordPredicates = recordPredicates;

        if (!isFirstRowHeader) {
            createBuilderWithNumberedHeader();
        }
    }

    public StringTableBuilder getTableBuilder() {
        finishParsing();
        return builder;
    }

    public void parseRawLine(String rawLine) {
        if (!validateRawLine(rawLine)) {
            return;
        }

        if (builder == null) {
            createTableBuilderWithHeader(rawLine);
        } else {
            addLastRecord();
            setLastRecord(rawLine);
        }
    }

    private void createTableBuilderWithHeader(String rawLine) {
        if (!isFirstRowHeader) {
            throw new IllegalStateException("first row should be header");
        }
        if (lastRecord != null) {
            throw new IllegalStateException("lastRecord should be null");
        }
        builder = DefaultStringTableImpl.newBuilder(toUniqueStringArray(toStringArray(rawLine, fieldLengths)));
    }

    private void createBuilderWithNumberedHeader() {
        builder = DefaultStringTableImpl.newBuilder(StringTableFactory.getDefaultHeader(fieldLengths.length));
    }

    private boolean validateRawLine(String rawLine) {
        for (Predicate<String> linePredicate : linePredicates) {
            if (!linePredicate.test(rawLine.trim())) {
                return false;
            }
        }
        return true;
    }

    private void addLastRecord() {
        if (lastRecord == null) {
            return;
        }
        if (!validateLastRecord()) {
            return;
        }
        builder.addRecord(lastRecord);
    }

    private boolean validateLastRecord() {
        StringRecord tmpRec = DefaultStringRecordImpl.newRecord(builder.getSchema(), lastRecord);
        for (Predicate<StringRecord> recordPredicate : recordPredicates) {
            if (!recordPredicate.test(tmpRec)) {
                return false;
            }
        }
        return true;
    }

    private void finishParsing() {
        if (builder == null) {
            createBuilderWithNumberedHeader();
        }
        addLastRecord();
    }

    private void setLastRecord(String rawLine) {
        lastRecord = toStringArray(rawLine, fieldLengths);
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
