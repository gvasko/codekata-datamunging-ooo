package hu.gvasko.stringtable;

/**
 * Created by gvasko on 2015.05.07..
 */
public final class StringRecordFixtures {

    public static final String SINGLE_KEY = "singleKey";
    public static final String SINGLE_VALUE = "singleValue";

    public static final String TRIO1_KEY = "key-one";
    public static final String TRIO1_VALUE = "value-one";
    public static final String TRIO2_KEY = "key-two";
    public static final String TRIO2_VALUE = "value-two";
    public static final String TRIO3_KEY = "key-three";
    public static final String TRIO3_VALUE = "value-three";

    public static final String NUMBER0_KEY = "0";
    public static final String NUMBER0_VALUE = "zero";
    public static final String NUMBER1_KEY = "1";
    public static final String NUMBER1_VALUE = "one";
    public static final String NUMBER10_KEY = "10";
    public static final String NUMBER10_VALUE = "ten";

    public static StringRecord getSingleFieldRecord() {
        return DefaultStringRecordImpl.newBuilder().addField(SINGLE_KEY, SINGLE_VALUE).build();
    }

    public static StringRecord getTrioFieldsRecord() {
        return DefaultStringRecordImpl.newBuilder()
                .addField(TRIO1_KEY, TRIO1_VALUE)
                .addField(TRIO2_KEY, TRIO2_VALUE)
                .addField(TRIO3_KEY, TRIO3_VALUE).build();
    }

    public static StringRecord getNumberBasedRecords() {
        return DefaultStringRecordImpl.newBuilder()
                .addField(NUMBER0_KEY, NUMBER0_VALUE)
                .addField(NUMBER1_KEY, NUMBER1_VALUE)
                .addField(NUMBER10_KEY, NUMBER10_VALUE).build();
    }
}
