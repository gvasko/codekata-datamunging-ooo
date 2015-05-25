package hu.gvasko.stringtable;

import java.io.Reader;
import java.util.Map;

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

    public static StringRecordBuilder getStringRecordBuilder(StringRecordFactory testDoubleRecordFactory) {
        return new DefaultStringRecordBuilderImpl.FactoryImpl(testDoubleRecordFactory).createNew();
    }

    public static StringRecord getStringRecord(Map<String, String> stringMap) {
        return new DefaultStringRecordImpl.FactoryImpl().createNew(stringMap);
    }

    public static StringTableBuilder getStringTableBuilder(StringTableFactory testDoubleTableFactory, String... schema) {
        return new DefaultStringTableBuilderImpl.FactoryImpl(testDoubleTableFactory).createNew(schema);
    }

    public static StringTableParser getStringTableParser(
            TableParserLogicFactory testDoubleLogicFactory,
            StringRecordParser testDoubleRecParser,
            Reader testDoubleReader) {
        return new DefaultTableParserContextImpl.FactoryImpl(testDoubleLogicFactory).createNew(testDoubleRecParser, testDoubleReader);
    }
}
