package hu.gvasko.stringtable.defaultimpl;

import com.google.inject.Inject;
import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultTableParserLineReaderImpl implements StringTableParser {

    static class FactoryImpl implements StringTableParserFactory {

        private TableParserLogicFactory logicFactory;

        @Inject
        FactoryImpl(TableParserLogicFactory logicFactory) {
            this.logicFactory = logicFactory;
        }

        @Override
        public StringTableParser createNew(StringRecordParser recordParser, URI uri) throws IOException {
            return new DefaultTableParserLineReaderImpl(logicFactory, recordParser, uri);
        }

        @Override
        public StringTableParser createNew(StringRecordParser recordParser, Reader reader) {
            return new DefaultTableParserLineReaderImpl(logicFactory, recordParser, reader);
        }
    }

    private BufferedReader reader;
    private boolean isFirstRowHeader = false;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private TableParserLogicFactory logicFactory;
    private StringRecordParser recordParser;

    private DefaultTableParserLineReaderImpl(TableParserLogicFactory sharedLogicFactory, StringRecordParser sharedRecordParser, URI fileLocation) throws IOException {
        this(sharedLogicFactory, sharedRecordParser, Files.newBufferedReader(Paths.get(fileLocation)));
    }

    private DefaultTableParserLineReaderImpl(TableParserLogicFactory sharedLogicFactory, StringRecordParser sharedRecordParser, Reader sharedReader) {
        reader = new BufferedReader(sharedReader);
        lineFilters = new ArrayList<>();
        recordFilters = new ArrayList<>();
        logicFactory = sharedLogicFactory;
        recordParser = sharedRecordParser;
    }

    @Override
    public StringTable parse() {
        try {
            return parseWithoutTry();
        } catch (IOException exReadLine) {
            throw new RuntimeException("Parse exception", exReadLine);
        }
    }

    private StringTable parseWithoutTry() throws IOException {
        TableParserLogic tableParserLogic = logicFactory.createNew(recordParser, isFirstRowHeader, lineFilters, recordFilters);
        String line;
        while ((line = reader.readLine()) != null) {
            tableParserLogic.parseRawLine(line);
        }
        return tableParserLogic.getTable();
    }

    @Override
    public StringTableParser firstRowIsHeader() {
        isFirstRowHeader = true;
        return this;
    }

    @Override
    public void addLineFilter(Predicate<String> linePredicate) {
        lineFilters.add(linePredicate);
    }

    @Override
    public void addRecordFilter(Predicate<StringRecord> recordPredicate) {
        recordFilters.add(recordPredicate);
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}