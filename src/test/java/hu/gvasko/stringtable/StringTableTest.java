package hu.gvasko.stringtable;

import org.junit.Assert;
import org.junit.Test;

import static hu.gvasko.stringtable.StringTableFixtures.*;


/**
 * Created by gvasko on 2015.05.07..
 */
public class StringTableTest {

    @Test
    public void emptyTableReturnsEmptyList() {
        StringTable table = getEmptyTable();
        Assert.assertArrayEquals(new StringRecord[0], table.getAllRecords().toArray());
    }

    @Test
    public void returnsAllRecords() {
        StringTable table = getAbcTable();
        String[] actualRecords = table.getAllRecords().stream().map(rec -> rec.toString()).toArray(String[]::new);
        String[] expectedRecords = { ROW_1_CSV, ROW_2_CSV, ROW_3_CSV, ROW_4_CSV };
        Assert.assertArrayEquals(expectedRecords, actualRecords);
    }

    @Test
    public void returnsDecodedRecords() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        table.addStringDecoder(value -> BBB_3_VALUE.equals(value) ? replacedValue : value, defaultSchema[BBB_COLUMN]);
        String[] actualRecords = table.getAllRecords().stream().map(rec -> rec.toString()).toArray(String[]::new);
        final String new_row_3_csv = AAA_3_VALUE + ',' + replacedValue + ',' + CCC_3_VALUE;
        String[] expectedRecords = { ROW_1_CSV, ROW_2_CSV, new_row_3_csv, ROW_4_CSV };
        Assert.assertArrayEquals(expectedRecords, actualRecords);
    }

    @Test
    public void multipleDecodersAreChained() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        table.addStringDecoder(value -> BBB_3_VALUE.equals(value) ? replacedValue : value, defaultSchema[BBB_COLUMN]);
        table.addStringDecoder(value -> value.replace("replaced", "new"), defaultSchema[BBB_COLUMN]);
        String[] actualRecords = table.getAllRecords().stream().map(rec -> rec.toString()).toArray(String[]::new);
        final String new_row_3_csv = AAA_3_VALUE + ',' + "new-value" + ',' + CCC_3_VALUE;
        String[] expectedRecords = { ROW_1_CSV, ROW_2_CSV, new_row_3_csv, ROW_4_CSV };
        Assert.assertArrayEquals(expectedRecords, actualRecords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assigningDecoderToUndefinedFieldThrowsException() {
        StringTable table = getAbcTable();
        table.addStringDecoder(value -> "", "invalid-column");
    }

    @Test
    public void addingDecoderToEmptyTableIsAllowed() {
        StringTable table = getEmptyTable();
        table.addStringDecoder(value -> "", defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoder(null, defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderToInvalidColumnThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoder(null, "invalid-column");
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderWithoutColumnThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingDecoderWithoutColumnThrowsException() {
        StringTable table = getAbcTable();
        table.addStringDecoder(value -> "");
    }

}
