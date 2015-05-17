package hu.gvasko.stringtable;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

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

    private Injector injector;

    protected StringTableFactory() {
        injector = Guice.createInjector(getStringTableModule());
    }

    public StringTableParser getFixWidthParser(URI uri) throws IOException {
        return new DefaultTableParserContextImpl(uri);
    }

    public StringTableParser getFixWidthParser(Reader reader) {
        return new DefaultTableParserContextImpl(reader);
    }


    // TODO: move to somewhere else?

    public UnaryOperator<String> keepIntegersOnly() {
        return s -> {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            return sb.toString();
        };
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

    StringRecordBuilder newStringRecordBuilder() {
        return newStringRecordBuilderFactory().createNew();
    }

    StringRecordBuilderFactory newStringRecordBuilderFactory() {
        return injector.getInstance(StringRecordBuilderFactory.class);
    }

    StringTableBuilder newStringTableBuilder(String... schema) {
        return newStringTableBuilderFactory().createNew(schema);
    }

    StringTableBuilderFactory newStringTableBuilderFactory() {
        return injector.getInstance(StringTableBuilderFactory.class);
    }

    private AbstractModule getStringTableModule() {
        AbstractModule module = new AbstractModule() {
            @Override
            protected void configure() {
                // ONLY FACTORIES???
                bind(StringRecordBuilderFactory.class).to(DefaultStringRecordImpl.BuilderFactoryImpl.class);
                bind(StringTableBuilderFactory.class).to(DefaultStringTableImpl.BuilderFactoryImpl.class);
            }
        };
        return module;
    }

}
