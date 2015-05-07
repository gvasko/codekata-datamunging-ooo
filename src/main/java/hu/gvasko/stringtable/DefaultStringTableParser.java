package hu.gvasko.stringtable;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Gvasko on 2015.05.07..
 */
class DefaultStringTableParser implements StringTableParser {

    private BufferedReader reader;
    private boolean firstRowIsHeader = false;
    private boolean excludeLastRow = false;
    private boolean excludeEmptyRows = false;

    public DefaultStringTableParser(URI fileLocation) throws IOException {
        reader = Files.newBufferedReader(Paths.get(fileLocation));
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
