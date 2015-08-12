package hu.gvasko.stringtable.defaultimpl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.06..
 */
public class DefaultMainTableFactoryImpl implements MainTableFactory {
    private Module module;
    private Injector injector;

    public DefaultMainTableFactoryImpl(Module stringRecordModule) {
        module = createGuiceModule();
        injector = Guice.createInjector(stringRecordModule, module);
    }

    public static Module createGuiceModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                // TODO: ONLY CTORS???
                bind(StringTableConstructorDelegate.class).to(DefaultStringTableImpl.ConstructorDelegateImpl.class);
                bind(StringTableBuilderConstructorDelegate.class).to(DefaultStringTableBuilderImpl.ConstructorDelegateImpl.class);
                bind(TableParserLogicConstructorDelegate.class).to(DefaultTableParserLogicImpl.ConstructorDelegateImpl.class);
                bind(StringTableParserConstructorDelegate.class).to(DefaultTableParserLineReaderImpl.ConstructorDelegateImpl.class);
            }
        };
    }

    public Module getGuiceModule() {
        return module;
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

    @Override
    public StringTableBuilder newStringTableBuilder(String... schema) {
        return getStringTableBuilderCtor().call(schema);
    }

    StringTableBuilderConstructorDelegate getStringTableBuilderCtor() {
        return injector.getInstance(StringTableBuilderConstructorDelegate.class);
    }

    TableParserLogic newTableParserLogic(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters) {
        return getTableParserLogicCtor().call(sharedRecParser, isFirstRowHeader, sharedLineFilters, sharedRecordFilters);
    }

    TableParserLogicConstructorDelegate getTableParserLogicCtor() {
        return injector.getInstance(TableParserLogicConstructorDelegate.class);
    }

    @Override
    public StringTableParser newStringTableParser(StringRecordParser recordParser, URI uri) throws IOException {
        return getStringTableParserCtor().call(recordParser, uri);
    }

    @Override
    public StringTableParser newStringTableParser(StringRecordParser recordParser, Reader reader) {
        return getStringTableParserCtor().call(recordParser, reader);
    }

    StringTableParserConstructorDelegate getStringTableParserCtor() {
        return injector.getInstance(StringTableParserConstructorDelegate.class);
    }

    static String[] getDefaultHeader(int length) {
        String[] numberedHeader = new String[length];
        for (int i = 0; i < length; i++) {
            numberedHeader[i] = Integer.toString(i);
        }
        return numberedHeader;
    }


}
