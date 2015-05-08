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
class DefaultStringTableParserImpl implements StringTableParser {

    private BufferedReader reader;
    private boolean isFirstRowHeader = false;
    private List<Predicate<String>> linePredicates;
    private List<Predicate<StringRecord>> recordPredicates;

    public DefaultStringTableParserImpl(URI fileLocation) throws IOException {
        this(Files.newBufferedReader(Paths.get(fileLocation)));
    }

    public DefaultStringTableParserImpl(Reader sharedReader) {
        reader = new BufferedReader(sharedReader);
        linePredicates = new ArrayList<>();
        recordPredicates = new ArrayList<>();
    }

    @Override
    public StringTable parse(int... fieldLengths) {
        try {
            return parseWithoutTry(fieldLengths);
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

    private StringTable parseWithoutTry(int[] fieldLengths) throws IOException {
        TableRawLineParser lineParser = new TableRawLineParser(fieldLengths, isFirstRowHeader, linePredicates, recordPredicates);
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
        linePredicates.add(linePredicate);
    }

    @Override
    public void addRecordFilter(Predicate<StringRecord> recordPredicate) {
        recordPredicates.add(recordPredicate);
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
