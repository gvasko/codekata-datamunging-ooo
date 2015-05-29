package hu.gvasko.stringtable;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.22..
 */
public final class UnitTestFixtures {

    private UnitTestFixtures() {
        // no instances
    }

    public static StringRecordParser getFixWidthRecordParser(int[] fieldLengths) {
        return new FixWidthTextParserImpl(fieldLengths);
    }

    public static StringRecordParser getCSVParser(int fieldCount) {
        return new CSVParserImpl(fieldCount);
    }

    public static StringRecordBuilder getStringRecordBuilder(StringRecordFactory testDoubleRecordFactory) {
        return new DefaultStringRecordBuilderImpl.FactoryImpl(testDoubleRecordFactory).createNew();
    }

    public static StringRecord getStringRecord(Map<String, String> stringMap) {
        return new DefaultStringRecordImpl.FactoryImpl().createNew(stringMap);
    }

    public static StringTableBuilder getStringTableBuilder(StringTableFactory testDoubleTableFactory, String... schema) {
        return new DefaultStringTableBuilderImpl.FactoryImpl(testDoubleTableFactory).createNewTableBuilder(schema);
    }

    public static StringTableParser getStringTableParser(
            TableParserLogicFactory testDoubleLogicFactory,
            StringRecordParser testDoubleRecParser,
            Reader testDoubleReader) {
        return new DefaultTableParserLineReaderImpl.FactoryImpl(testDoubleLogicFactory).createNew(testDoubleRecParser, testDoubleReader);
    }

    public static StringTable getStringTable(StringRecordBuilderFactory testDoubleRecBuilderFactory, String[] schema, List<String[]> records) {
        return new DefaultStringTableImpl.FactoryImpl(testDoubleRecBuilderFactory).createNew(schema, records);
    }

    public static TableParserLogic getTableParserLogic(StringTableBuilderFactory testDoubleTableBuilderFactory, StringRecordParser testDoubleRecParser) {
        return new DefaultTableParserLogicImpl.FactoryImpl(testDoubleTableBuilderFactory).createNew(testDoubleRecParser);
    }

    public static TableParserLogic getTableParserLogic(StringTableBuilderFactory testDoubleTableBuilderFactory, StringRecordParser testDoubleRecParser, boolean isFirstRowHeader, List<Predicate<String>> lineFilters, List<Predicate<StringRecord>> recordFilters) {
        return new DefaultTableParserLogicImpl.FactoryImpl(testDoubleTableBuilderFactory).createNew(testDoubleRecParser, isFirstRowHeader, lineFilters, recordFilters);
    }
}
