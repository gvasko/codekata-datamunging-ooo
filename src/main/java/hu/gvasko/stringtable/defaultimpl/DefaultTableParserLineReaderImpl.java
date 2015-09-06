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

    static class ConstructorDelegateImpl implements StringTableParserConstructorDelegate {

        private TableParserLogicConstructorDelegate logicCtor;

        @Inject
        ConstructorDelegateImpl(TableParserLogicConstructorDelegate logicCtor) {
            this.logicCtor = logicCtor;
        }

        @Override
        public StringTableParser call(StringRecordParser recordParser, URI uri) throws IOException {
            return new DefaultTableParserLineReaderImpl(logicCtor, recordParser, uri);
        }

        @Override
        public StringTableParser call(StringRecordParser recordParser, Reader reader) {
            return new DefaultTableParserLineReaderImpl(logicCtor, recordParser, reader);
        }
    }

    private BufferedReader reader;
    private boolean isFirstRowHeader = false;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private TableParserLogicConstructorDelegate logicCtor;
    private StringRecordParser recordParser;

    private DefaultTableParserLineReaderImpl(TableParserLogicConstructorDelegate sharedLogicCtor, StringRecordParser sharedRecordParser, URI fileLocation) throws IOException {
        // TODO: double buffer?
        this(sharedLogicCtor, sharedRecordParser, Files.newBufferedReader(Paths.get(fileLocation)));
    }

    private DefaultTableParserLineReaderImpl(TableParserLogicConstructorDelegate sharedLogicCtor, StringRecordParser sharedRecordParser, Reader sharedReader) {
        reader = new BufferedReader(sharedReader);
        lineFilters = new ArrayList<>();
        recordFilters = new ArrayList<>();
        logicCtor = sharedLogicCtor;
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
        TableParserLogic tableParserLogic = logicCtor.call(recordParser, isFirstRowHeader, lineFilters, recordFilters);
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
