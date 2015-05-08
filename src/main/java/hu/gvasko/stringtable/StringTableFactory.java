package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

/**
 * Created by gvasko on 2015.05.06..
 */
public class StringTableFactory {
    private static StringTableFactory soleInstance = new StringTableFactory();

    public static StringTableFactory getInstance() {
        return soleInstance;
    }

    static String[] getDefaultHeader(int length) {
        String[] numberedHeader = new String[length];
        for (int i = 0; i < length; i++) {
            numberedHeader[i] = Integer.toString(i);
        }
        return numberedHeader;
    }

    public StringTableParser getParser(URI uri) throws IOException {
        return new DefaultStringTableParserImpl(uri);
    }

    public StringTableParser getParser(Reader reader) {
        return new DefaultStringTableParserImpl(reader);
    }
}
