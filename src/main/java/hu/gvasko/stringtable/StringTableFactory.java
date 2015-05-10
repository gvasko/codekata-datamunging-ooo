package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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

    public StringTableParser getFixWidthParser(URI uri) throws IOException {
        return new DefaultTableParserContextImpl(uri);
    }

    public StringTableParser getFixWidthParser(Reader reader) {
        return new DefaultTableParserContextImpl(reader);
    }


    // TODO: move to somewhere else?

    public UnaryOperator<String> getKeepIntegerOnlyOperator() {
        return new KeepIntegerOnly();
    }

    public Predicate<String> skipEmptyLines() {
        return line -> !"".equals(line);
    }

    public Predicate<String> skipSplitterLines() {
        return line -> "".equals(line) || !"".equals(line.replace(line.charAt(0), ' ').trim());
    }

    public Predicate<StringRecord> onlyNumbersInColumn(String column) {
        return record -> record.get(column).matches("^[+-]?\\d+$");
    }

}
