package hu.gvasko.stringtable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Gvasko on 2015.05.08..
 */
class TableRawLineParser {

    private StringTableBuilder builder = null;
    private StringRecordParser recParser;

    private boolean isFirstRowHeader;

    private String[] lastRecord = null;
    private List<Predicate<String>> linePredicates;
    private List<Predicate<StringRecord>> recordPredicates;

    public TableRawLineParser(
            int[] fieldLengths,
            boolean isFirstRowHeader,
            List<Predicate<String>> linePredicates,
            List<Predicate<StringRecord>> recordPredicates) {
        this.recParser = new FixWidthStringRecordParserImpl(fieldLengths);
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
        builder = DefaultStringTableImpl.newBuilder(recParser.parseHeader(rawLine));
    }

    private void createBuilderWithNumberedHeader() {
        builder = DefaultStringTableImpl.newBuilder(StringTableFactory.getDefaultHeader(recParser.getColumnCount()));
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
        lastRecord = recParser.parse(rawLine);
    }

}
