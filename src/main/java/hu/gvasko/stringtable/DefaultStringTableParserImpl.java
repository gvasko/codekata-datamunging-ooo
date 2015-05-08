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
    private boolean firstRowIsHeader = false;
    private boolean excludeLastRow = false;
    private boolean excludeEmptyRows = false;

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
        TableRawLineProcessor lineProcessor = new TableRawLineProcessor(fieldLengths, firstRowIsHeader, excludeLastRow, excludeEmptyRows);
        String line;
        while ((line = reader.readLine()) != null) {
            lineProcessor.processRawLine(line);
        }
        return lineProcessor.getTableBuilder().build();
    }

    @Override
    public StringTableParser firstRowIsHeader() {
        firstRowIsHeader = true;
        return this;
    }

    @Override
    public StringTableParser excludeLastRow() {
        excludeLastRow = true;
        return this;
    }

    @Override
    public StringTableParser excludeEmptyRows() {
        excludeEmptyRows = true;
        return this;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
