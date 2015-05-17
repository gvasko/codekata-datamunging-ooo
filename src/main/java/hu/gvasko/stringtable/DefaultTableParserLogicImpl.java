package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.List;
import java.util.function.Predicate;

/**
 * Processes lines of text
 * Handles header line
 * Handles filters
 * Created by Gvasko on 2015.05.08..
 */
class DefaultTableParserLogicImpl implements TableParserLogic {

    static class FactoryImpl implements TableParserLogicFactory {

        private StringTableBuilderFactory tableBuilderFactory;

        @Inject
        public FactoryImpl(StringTableBuilderFactory tableBuilderFactory) {
            this.tableBuilderFactory = tableBuilderFactory;
        }

        @Override
        public TableParserLogic createNew(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters) {
            return new DefaultTableParserLogicImpl(sharedRecParser, isFirstRowHeader, sharedLineFilters, sharedRecordFilters, tableBuilderFactory);
        }
    }

    private StringTableBuilder builder = null;
    private StringRecordParser recParser;

    private boolean isFirstRowHeader;

    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private StringTableBuilderFactory tableBuilderFactory;

    private DefaultTableParserLogicImpl(
            StringRecordParser sharedRecParser,
            boolean isFirstRowHeader,
            List<Predicate<String>> lineFilters,
            List<Predicate<StringRecord>> recordFilters,
            StringTableBuilderFactory sharedTableBuilderFactory) {
        this.isFirstRowHeader = isFirstRowHeader;
        this.lineFilters = lineFilters;
        this.recordFilters = recordFilters;
        this.recParser = sharedRecParser;
        this.tableBuilderFactory = sharedTableBuilderFactory;

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
        builder = tableBuilderFactory.createNew(recParser.parseHeader(rawLine));
    }

    private void createBuilderWithNumberedHeader() {
        builder = tableBuilderFactory.createNew(StringTableFactory.getDefaultHeader(recParser.getColumnCount()));
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
        StringRecord tmpRec = tableBuilderFactory.getRecordBuilderFactory().createNew().addFields(builder.getSchema(), record).build();
        for (Predicate<StringRecord> recordPredicate : recordFilters) {
            if (!recordPredicate.test(tmpRec)) {
                return false;
            }
        }
        return true;
    }

}
