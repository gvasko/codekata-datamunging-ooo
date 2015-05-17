package hu.gvasko.stringtable;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static hu.gvasko.stringtable.StringTableFixtures.*;


/**
 * Created by gvasko on 2015.05.07..
 */
public class StringTableTest {

    interface RecordSupplier {
        StringRecord getRecordAtRow(int row);
    }

    interface RawRecordSupplier {
        String[] getRecordAtRow(int row);
    }

    @Test
    public void emptyTableReturnsEmptyList() {
        StringTable table = getEmptyTable();
        Assert.assertArrayEquals(new StringRecord[0], table.getAllRecords().toArray());
    }

    @Test
    public void returnsRecordAtIndex() {
        StringTable table = getAbcTable();
        Assert.assertEquals("Row count: ", abcTable.length, table.getRowCount());
        assertRowsEquals(table, row -> abcTable[row], row -> table.getRecord(row));
    }

    @Test
    public void returnsAllRecords() {
        StringTable table = getAbcTable();
        List<StringRecord> records = table.getAllRecords();
        Assert.assertEquals("Row count: ", abcTable.length, records.size());
        assertRowsEquals(table, row -> abcTable[row], row -> records.get(row));
    }

    @Test
    public void recordAtIndexDecoded() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        final String testValue = abcTable[testRow][testCol];
        table.addStringDecoderToColumns(value -> testValue.equals(value) ? replacedValue : value, defaultSchema[testCol]);

        assertRowsEquals(table, row -> row != testRow ? abcTable[row] : copyAndReplace(abcTable[row], testCol, replacedValue), row -> table.getRecord(row));
    }

    @Test
    public void allRecordsDecoded() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        final String testValue = abcTable[testRow][testCol];
        table.addStringDecoderToColumns(value -> testValue.equals(value) ? replacedValue : value, defaultSchema[testCol]);

        List<StringRecord> records = table.getAllRecords();
        assertRowsEquals(table, row -> row != testRow ? abcTable[row] : copyAndReplace(abcTable[row], testCol, replacedValue), row -> records.get(row));
    }

    private static String[] copyAndReplace(String[] rawRecord, int col, String newValue) {
        String[] tmp = Arrays.copyOf(rawRecord, rawRecord.length);
        tmp[col] = newValue;
        return tmp;
    }

    private static void assertRowsEquals(StringTable table, RawRecordSupplier expectedRecSupplier, RecordSupplier actualRecSupplier) {
        for (int row = 0; row < table.getRowCount(); row++) {
            String[] expectedRecord = expectedRecSupplier.getRecordAtRow(row);
            StringRecord actualRecord = actualRecSupplier.getRecordAtRow(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = getMessage(row, col);
                Assert.assertEquals(message, expectedRecord[col], actualRecord.get(defaultSchema[col]));
            }
        }
    }

    private static String getMessage(int row, int col) {
        return "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
    }

    @Test
    public void multipleDecodersAreChained() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        table.addStringDecoderToColumns(value -> abcTable[testRow][testCol].equals(value) ? replacedValue : value, defaultSchema[testCol]);
        table.addStringDecoderToColumns(value -> value.replace("replaced", "new"), defaultSchema[testCol]);

        Assert.assertEquals("new-value", table.getRecord(testRow).get(defaultSchema[testCol]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assigningDecoderToUndefinedFieldThrowsException() {
        StringTable table = getAbcTable();
        table.addStringDecoderToColumns(value -> "", "invalid-column");
    }

    @Test
    public void addingDecoderToEmptyTableIsAllowed() {
        StringTable table = getEmptyTable();
        table.addStringDecoderToColumns(value -> "", defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoderToColumns(null, defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderToInvalidColumnThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoderToColumns(null, "invalid-column");
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderWithoutColumnThrowsNPE() {
        StringTable table = getAbcTable();
        table.addStringDecoderToColumns(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingDecoderWithoutColumnThrowsException() {
        StringTable table = getAbcTable();
        table.addStringDecoderToColumns(value -> "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void schemaMustBeUnique() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        stringTableFactory.newStringTableBuilderFactory().createNew(new String[]{"A", "B", "A"});
    }

    @Test
    public void singleRowTable() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        StringTableParser tableParser = stringTableFactory.newStringTableParser(new StringReader("A B C "));
        StringTable singleRowTable = tableParser.parse(stringTableFactory.getFixWidthRecordParser(2, 2, 2));
        Assert.assertEquals("Row count", 1, singleRowTable.getRowCount());
        StringRecord theRecord = singleRowTable.getRecord(0);
        Assert.assertEquals("element 0", "A", theRecord.get("0"));
        Assert.assertEquals("element 1", "B", theRecord.get("1"));
        Assert.assertEquals("element 2", "C", theRecord.get("2"));
    }

}
