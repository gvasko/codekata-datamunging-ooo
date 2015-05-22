package hu.gvasko.stringtable;

import com.google.inject.AbstractModule;

/**
 * Created by gvasko on 2015.05.22..
 */
public final class UnitTestFixtures {

    private UnitTestFixtures() {
        // no instances
    }

    public static StringRecordParser getFixWidthRecordParser(int[] fieldLengths) {
        return new FixWidthStringRecordParserImpl(fieldLengths);
    }

    public static StringRecordBuilder getStringRecordBuilder(StringRecordFactory testDoubleRecordFactory) {
        return new DefaultStringRecordBuilderImpl.FactoryImpl(testDoubleRecordFactory).createNew();
    }
}
