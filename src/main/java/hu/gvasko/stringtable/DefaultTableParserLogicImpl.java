package hu.gvasko.stringtable;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    private StringTableBuilder _builder = null;
    private StringRecordParser recParser;
    private Boolean _firstRowHeader = null;
    private Queue<String> lineBuffer;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private StringTableBuilderFactory tableBuilderFactory;

    private DefaultTableParserLogicImpl(
            StringRecordParser sharedRecParser,
            boolean firstRowHeader,
            List<Predicate<String>> lineFilters,
            List<Predicate<StringRecord>> recordFilters,
            StringTableBuilderFactory sharedTableBuilderFactory) {
        this.recParser = sharedRecParser;
        this.lineFilters = lineFilters;
        this.recordFilters = recordFilters;
        this.tableBuilderFactory = sharedTableBuilderFactory;
        setFirstRowHeader(firstRowHeader);
        this.lineBuffer = new LinkedList<>();
    }

    private DefaultTableParserLogicImpl(
            StringRecordParser sharedRecParser,
            StringTableBuilderFactory sharedTableBuilderFactory) {
        this.lineFilters = new ArrayList<>();
        this.recordFilters = new ArrayList<>();
        this.recParser = sharedRecParser;
        this.tableBuilderFactory = sharedTableBuilderFactory;
        this.lineBuffer = new LinkedList<>();
    }

    @Override
    public StringTable getTable() {
        parseLineBuffer_lazy();
        return getBuilder_lazy().build();
    }

    @Override
    public boolean isFirstRowHeader() {
        if (_firstRowHeader == null) {
            _firstRowHeader = Boolean.FALSE;
        }
        return _firstRowHeader;
    }

    @Override
    public void setFirstRowHeader(boolean f) {
        if (_firstRowHeader == null) {
            _firstRowHeader = f;
        }
    }

    @Override
    public void parseRawLine(String rawLine) {
        if (!validateRawLine(rawLine)) {
            return;
        }
        lineBuffer.add(rawLine);
        parseLineBuffer_lazy();
    }

    private void parseLineBuffer_lazy() {
        StringTableBuilder builder = getBuilder_lazy();
        while (!lineBuffer.isEmpty()) {
            String[] record = recParser.parseRecord(lineBuffer.remove());
            if (validateRecord(record)) {
                builder.addRecord(record);
            }
        }
    }

    private StringTableBuilder getBuilder_lazy() {
        if (_builder != null) {
            return _builder;
        }

        if (isFirstRowHeader()) {
            if (lineBuffer.isEmpty()) {
                throw new IllegalArgumentException("Missing table header");
            }
            _builder = createTableBuilderWithHeader(lineBuffer.remove());
        } else {
            _builder = createBuilderWithNumberedHeader();
        }

        if (_builder == null) {
            throw new IllegalStateException("Table builder cannot be null.");
        }

        return _builder;
    }

    private StringTableBuilder createTableBuilderWithHeader(String rawLine) {
        return tableBuilderFactory.createNewTableBuilder(parseHeader(rawLine));
    }

    private String[] parseHeader(String rawLine) {
        return ensureUniqueElements(recParser.parseRecord(rawLine));
    }

    private StringTableBuilder createBuilderWithNumberedHeader() {
        return tableBuilderFactory.createNewTableBuilder(DefaultFactory.getDefaultHeader(recParser.getColumnCount()));
    }

    private boolean validateRawLine(String rawLine) {
        for (Predicate<String> linePredicate : lineFilters) {
            if (!linePredicate.test(rawLine.trim())) {
                return false;
            }
        }
        return true;
    }

    private boolean validateRecord(String[] record) {
        StringRecord tmpRec = tableBuilderFactory.createNewRecordBuilder().addFields(getBuilder_lazy().getSchema(), record).build();
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
