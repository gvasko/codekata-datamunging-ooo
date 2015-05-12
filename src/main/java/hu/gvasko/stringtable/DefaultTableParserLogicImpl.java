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

        if (isFirstRowHeader) {
            // create builder when first row is available
        } else {
            createBuilderWithNumberedHeader();
        }
    }

    @Override
    public StringTableBuilder getTableBuilder() {
        if (builder == null) {
            createBuilderWithNumberedHeader();
        }
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
            String[] record = recParser.parseRecord(rawLine);
            if (validateLastRecord(record)) {
                builder.addRecord(record);
            }
        }
    }

    private void createTableBuilderWithHeader(String rawLine) {
        if (!isFirstRowHeader) {
            throw new IllegalStateException("first row should be header");
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

    private boolean validateLastRecord(String[] record) {
        StringRecord tmpRec = DefaultStringRecordImpl.newRecord(builder.getSchema(), record);
        for (Predicate<StringRecord> recordPredicate : recordFilters) {
            if (!recordPredicate.test(tmpRec)) {
                return false;
            }
        }
        return true;
    }

}
