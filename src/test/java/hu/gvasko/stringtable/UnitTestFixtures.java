package hu.gvasko.stringtable;

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
}
