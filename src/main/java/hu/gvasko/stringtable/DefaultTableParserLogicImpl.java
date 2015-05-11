package hu.gvasko.stringtable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Processes lines of text
 * Handles header line
 * Handles filters
 * Created by Gvasko on 2015.05.08..
 */
class DefaultTableParserLogicImpl implements TableParserLogic {

    private StringTableBuilder builder = null;
    private StringRecordParser recParser;

    private boolean isFirstRowHeader;

    private String[] lastRecord = null;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    public DefaultTableParserLogicImpl(
            int[] fieldLengths,
            boolean isFirstRowHeader,
            List<Predicate<String>> lineFilters,
            List<Predicate<StringRecord>> recordFilters) {
        this.recParser = new FixWidthStringRecordParserImpl(fieldLengths);
        this.isFirstRowHeader = isFirstRowHeader;
        this.lineFilters = lineFilters;
        this.recordFilters = recordFilters;

        if (!isFirstRowHeader) {
            createBuilderWithNumberedHeader();
        }
    }

    @Override
    public StringTableBuilder getTableBuilder() {
        finishParsing();
        return builder;
    }

    @Override
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
        for (Predicate<String> linePredicate : lineFilters) {
            if (!linePredicate.test(rawLine.trim())) {
                return false;
            }
        }
        return true;
    }

    private void setLastRecord(String rawLine) {
        lastRecord = recParser.parseRecord(rawLine);
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
        for (Predicate<StringRecord> recordPredicate : recordFilters) {
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

}
