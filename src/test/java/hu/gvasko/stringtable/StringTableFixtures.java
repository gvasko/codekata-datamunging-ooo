package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.07..
 */
public final class StringTableFixtures {

    public static final String[] defaultSchema = { "Aaa", "Bbb", "Ccc" };

    public static final int AAA_COLUMN = 0;
    public static final int BBB_COLUMN = 1;
    public static final int CCC_COLUMN = 2;

    public static final String AAA_1_VALUE = "a1";
    public static final String BBB_1_VALUE = "b1";
    public static final String CCC_1_VALUE = "c1";
    public static final String AAA_2_VALUE = "a2";
    public static final String BBB_2_VALUE = "b2";
    public static final String CCC_2_VALUE = "c2";
    public static final String AAA_3_VALUE = "a3";
    public static final String BBB_3_VALUE = "b3";
    public static final String CCC_3_VALUE = "c3";
    public static final String AAA_4_VALUE = "a4";
    public static final String BBB_4_VALUE = "b4";
    public static final String CCC_4_VALUE = "c4";

    public static final String ROW_1_CSV = AAA_1_VALUE + ',' + BBB_1_VALUE + ',' + CCC_1_VALUE;
    public static final String ROW_2_CSV = AAA_2_VALUE + ',' + BBB_2_VALUE + ',' + CCC_2_VALUE;
    public static final String ROW_3_CSV = AAA_3_VALUE + ',' + BBB_3_VALUE + ',' + CCC_3_VALUE;
    public static final String ROW_4_CSV = AAA_4_VALUE + ',' + BBB_4_VALUE + ',' + CCC_4_VALUE;

    public static StringTable getEmptyTable() {
        return DefaultStringTableImpl.newBuilder(defaultSchema).build();
    }

    public static StringTable getAbcTable() {
        return DefaultStringTableImpl.newBuilder(defaultSchema)
                .addRecord(AAA_1_VALUE, BBB_1_VALUE, CCC_1_VALUE)
                .addRecord(AAA_2_VALUE, BBB_2_VALUE, CCC_2_VALUE)
                .addRecord(AAA_3_VALUE, BBB_3_VALUE, CCC_3_VALUE)
                .addRecord(AAA_4_VALUE, BBB_4_VALUE, CCC_4_VALUE).build();
    }

}
