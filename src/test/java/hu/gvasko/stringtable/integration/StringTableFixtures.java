package hu.gvasko.stringtable.integration;

import hu.gvasko.stringrecord.defaultimpl.DefaultMainRecordFactoryImpl;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.defaultimpl.DefaultMainTableFactoryImpl;
import hu.gvasko.stringtable.StringTableBuilder;

/**
 * Created by gvasko on 2015.05.07..
 */
public final class StringTableFixtures {

    public static final int AAA_COLUMN = 0;

    public static final int CCC_COLUMN = 2;
    public static final int BBB_COLUMN = 1;

    public static String[] getDefaultSchema() {
        return new String[] { "Aaa", "Bbb", "Ccc" };
    }

    public static String[][] getAbcArrays() {
        return new String[][] {
                { "a0", "b0", "c0" },
                { "a1", "b1", "c1" },
                { "a2", "b2", "c2" },
                { "a3", "b3", "c3" }
        };
    }

    public static StringTable getEmptyTable() {
        return new DefaultMainTableFactoryImpl(DefaultMainRecordFactoryImpl.createGuiceModule()).newStringTableBuilder(getDefaultSchema()).build();
    }

    public static StringTable getAbcTable() {
        StringTableBuilder builder = new DefaultMainTableFactoryImpl(DefaultMainRecordFactoryImpl.createGuiceModule()).newStringTableBuilder(getDefaultSchema());

        String[][] abcTable = getAbcArrays();
        for (int i = 0; i < abcTable.length; i++) {
            builder.addRecord(abcTable[i][AAA_COLUMN], abcTable[i][BBB_COLUMN], abcTable[i][CCC_COLUMN]);
        }
        return builder.build();
    }

}