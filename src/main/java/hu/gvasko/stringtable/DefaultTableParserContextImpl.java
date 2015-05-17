package hu.gvasko.stringtable;

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
class DefaultTableParserContextImpl implements StringTableParser {

    private BufferedReader reader;
    private boolean isFirstRowHeader = false;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    private TableParserLogicFactory logicFactory;

    DefaultTableParserContextImpl(TableParserLogicFactory sharedLogicFactory, URI fileLocation) throws IOException {
        this(sharedLogicFactory, Files.newBufferedReader(Paths.get(fileLocation)));
    }

    DefaultTableParserContextImpl(TableParserLogicFactory sharedLogicFactory, Reader sharedReader) {
        reader = new BufferedReader(sharedReader);
        lineFilters = new ArrayList<>();
        recordFilters = new ArrayList<>();
        logicFactory = sharedLogicFactory;
    }

    @Override
    public StringTable parse(StringRecordParser recordParser) {
        try {
            return parseWithoutTry(recordParser);
        } catch (IOException exReadLine) {
            RuntimeException ex = new RuntimeException("Parse exception", exReadLine);
            try {
                close();
            }
            catch (Exception exClose) {
                ex.addSuppressed(exClose);
            }
            throw ex;
        }
    }

    private StringTable parseWithoutTry(StringRecordParser recordParser) throws IOException {
        TableParserLogic lineParser = logicFactory.createNew(recordParser, isFirstRowHeader, lineFilters, recordFilters);
        String line;
        while ((line = reader.readLine()) != null) {
            lineParser.parseRawLine(line);
        }
        return lineParser.getTableBuilder().build();
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
