package hu.gvasko.stringtable;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.06..
 */
public class DefaultFactory {
    private static DefaultFactory soleInstance = new DefaultFactory();

    public static DefaultFactory getInstance() {
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

    protected DefaultFactory() {
        injector = Guice.createInjector(getStringTableModule());
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

    public StringRecordParser getFixWidthRecordParser(int... fieldLengths) {
        return new FixWidthTextParserImpl(fieldLengths);
    }

    public StringRecordParser getCSVRecordParser(int columnCount) {
        return new CSVParserImpl(columnCount);
    }

    StringRecordBuilder newStringRecordBuilder() {
        return newStringRecordBuilderFactory().createNew();
    }

    StringRecordBuilderFactory newStringRecordBuilderFactory() {
        return injector.getInstance(StringRecordBuilderFactory.class);
    }

    StringTableBuilder newStringTableBuilder(String... schema) {
        return newStringTableBuilderFactory().createNewTableBuilder(schema);
    }

    StringTableBuilderFactory newStringTableBuilderFactory() {
        return injector.getInstance(StringTableBuilderFactory.class);
    }

    TableParserLogic newTableParserLogic(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters) {
        return newTableParserLogicFactory().createNew(sharedRecParser, isFirstRowHeader, sharedLineFilters, sharedRecordFilters);
    }

    TableParserLogicFactory newTableParserLogicFactory() {
        return injector.getInstance(TableParserLogicFactory.class);
    }

    public StringTableParser newStringTableParser(StringRecordParser recordParser, URI uri) throws IOException {
        return newStringTableParserFactory().createNew(recordParser, uri);
    }

    public StringTableParser newStringTableParser(StringRecordParser recordParser, Reader reader) {
        return newStringTableParserFactory().createNew(recordParser, reader);
    }

    StringTableParserFactory newStringTableParserFactory() {
        return injector.getInstance(StringTableParserFactory.class);
    }

    private AbstractModule getStringTableModule() {
        AbstractModule module = new AbstractModule() {
            @Override
            protected void configure() {
                // TODO: ONLY FACTORIES???
                bind(StringRecordFactory.class).to(DefaultStringRecordImpl.FactoryImpl.class);
                bind(StringRecordBuilderFactory.class).to(DefaultStringRecordBuilderImpl.FactoryImpl.class);
                bind(StringTableFactory.class).to(DefaultStringTableImpl.FactoryImpl.class);
                bind(StringTableBuilderFactory.class).to(DefaultStringTableBuilderImpl.FactoryImpl.class);
                bind(TableParserLogicFactory.class).to(DefaultTableParserLogicImpl.FactoryImpl.class);
                bind(StringTableParserFactory.class).to(DefaultTableParserLineReaderImpl.FactoryImpl.class);
            }
        };
        return module;
    }

}
