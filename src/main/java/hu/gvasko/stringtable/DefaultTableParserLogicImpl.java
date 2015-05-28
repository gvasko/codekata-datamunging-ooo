package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.ArrayList;
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
        public TableParserLogic createNew(StringRecordParser sharedRecParser) {
            return new DefaultTableParserLogicImpl(sharedRecParser, tableBuilderFactory);
        }

        @Override
        public TableParserLogic createNew(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters) {
            return new DefaultTableParserLogicImpl(sharedRecParser, isFirstRowHeader, sharedLineFilters, sharedRecordFilters, tableBuilderFactory);
        }
    }

    private StringTableBuilder builder = null;
    private StringRecordParser recParser;

    private Boolean isFirstRowHeaderObj;

    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private StringTableBuilderFactory tableBuilderFactory;

    private DefaultTableParserLogicImpl(
            StringRecordParser sharedRecParser,
            boolean isFirstRowHeader,
            List<Predicate<String>> lineFilters,
            List<Predicate<StringRecord>> recordFilters,
            StringTableBuilderFactory sharedTableBuilderFactory) {
        this.isFirstRowHeaderObj = isFirstRowHeader;
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

    private DefaultTableParserLogicImpl(
            StringRecordParser sharedRecParser,
            StringTableBuilderFactory sharedTableBuilderFactory) {
        this.isFirstRowHeaderObj = null;
        this.lineFilters = new ArrayList<>();
        this.recordFilters = new ArrayList<>();
        this.recParser = sharedRecParser;
        this.tableBuilderFactory = sharedTableBuilderFactory;
    }

    @Override
    public StringTable getTable() {
        if (builder == null) {
            createBuilderWithNumberedHeader();
        }
        return builder.build();
    }

    @Override
    public boolean isFirstRowHeader() {
        if (isFirstRowHeaderObj == null) {
            isFirstRowHeaderObj = Boolean.FALSE;
        }
        return isFirstRowHeaderObj;
    }

    @Override
    public void setFirstRowHeader(boolean f) {
        if (isFirstRowHeaderObj == null) {
            isFirstRowHeaderObj = f;
        }
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
        if (!isFirstRowHeader()) {
            throw new IllegalStateException("first row should be header");
        }
        builder = tableBuilderFactory.createNew(parseHeader(rawLine));
    }

    private String[] parseHeader(String rawLine) {
        return ensureUniqueElements(recParser.parseRecord(rawLine));
    }

    private void createBuilderWithNumberedHeader() {
        builder = tableBuilderFactory.createNew(DefaultFactory.getDefaultHeader(recParser.getColumnCount()));
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
        StringRecord tmpRec = tableBuilderFactory.getTableFactory().getRecordBuilderFactory().createNew().addFields(builder.getSchema(), record).build();
        for (Predicate<StringRecord> recordPredicate : recordFilters) {
            if (!recordPredicate.test(tmpRec)) {
                return false;
            }
        }
        return true;
    }

    private static String[] ensureUniqueElements(String[] stringArray) {
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
