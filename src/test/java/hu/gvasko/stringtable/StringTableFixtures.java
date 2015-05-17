package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.07..
 */
public final class StringTableFixtures {

    public static final String[] defaultSchema = { "Aaa", "Bbb", "Ccc" };

    public static final int AAA_COLUMN = 0;
    public static final int BBB_COLUMN = 1;
    public static final int CCC_COLUMN = 2;

    public static final String[][] abcTable = {
            { "a0", "b0", "c0" },
            { "a1", "b1", "c1" },
            { "a2", "b2", "c2" },
            { "a3", "b3", "c3" }
    };

    public static StringTable getEmptyTable() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        return stringTableFactory.newStringTableBuilderFactory().createNew(defaultSchema).build();
    }

    public static StringTable getAbcTable() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        StringTableBuilder builder = stringTableFactory.newStringTableBuilderFactory().createNew(defaultSchema);

        for (int i = 0; i < abcTable.length; i++) {
            builder.addRecord(abcTable[i][AAA_COLUMN], abcTable[i][BBB_COLUMN], abcTable[i][CCC_COLUMN]);
        }
        return builder.build();
    }

}
