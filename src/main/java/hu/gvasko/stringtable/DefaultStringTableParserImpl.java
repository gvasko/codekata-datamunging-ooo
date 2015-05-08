package hu.gvasko.stringtable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringTableParserImpl implements StringTableParser {

    private BufferedReader reader;
    private boolean isFirstRowHeader = false;
    private boolean isLastRowExcluded = false;
    private boolean isEmptyRowExcluded = false;

    public DefaultStringTableParserImpl(URI fileLocation) throws IOException {
        reader = Files.newBufferedReader(Paths.get(fileLocation));
    }

    public DefaultStringTableParserImpl(Reader sharedReader) {
        reader = new BufferedReader(sharedReader);
    }

    @Override
    public StringTable parse(int... fieldLengths) {
        try {
            return parseWithoutTry(fieldLengths);
        } catch (IOException exReadlLine) {
            RuntimeException ex = new RuntimeException("Parse exception", exReadlLine);
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
        TableRawLineParser lineParser = new TableRawLineParser(fieldLengths, isFirstRowHeader, isLastRowExcluded, isEmptyRowExcluded);
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
    public StringTableParser excludeLastRow() {
        isLastRowExcluded = true;
        return this;
    }

    @Override
    public StringTableParser excludeEmptyRows() {
        isEmptyRowExcluded = true;
        return this;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
