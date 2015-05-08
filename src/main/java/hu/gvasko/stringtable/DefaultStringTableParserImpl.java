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

    private Reader reader;
    private boolean firstRowIsHeader = false;
    private boolean excludeLastRow = false;
    private boolean excludeEmptyRows = false;

    public DefaultStringTableParserImpl(URI fileLocation) throws IOException {
        reader = Files.newBufferedReader(Paths.get(fileLocation));
    }

    public DefaultStringTableParserImpl(Reader sharedReader) {
        reader = sharedReader;
    }

    @Override
    public StringTable parse(int... fieldLengths) {
        return null;
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
