package hu.gvasko.stringtable;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

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
    public void returnsRecordAtIndex() {
        StringTable table = getAbcTable();
        Assert.assertEquals("Row count: ", abcTable.length, table.getRowCount());
        for (int row = 0; row < table.getRowCount(); row++) {
            String[] expectedRecord = abcTable[row];
            StringRecord actualRecord = table.getRecord(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                Assert.assertEquals(message, expectedRecord[col], actualRecord.get(defaultSchema[col]));
            }
        }
    }

    @Test
    public void returnsAllRecords() {
        StringTable table = getAbcTable();
        List<StringRecord> records = table.getAllRecords();
        Assert.assertEquals("Row count: ", abcTable.length, records.size());
        for (int row = 0; row < records.size(); row++) {
            String[] expectedRecord = abcTable[row];
            StringRecord actualRecord = records.get(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                Assert.assertEquals(message, expectedRecord[col], actualRecord.get(defaultSchema[col]));
            }
        }
    }

    @Test
    public void recordAtIndexDecoded() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        table.addStringDecoder(value -> abcTable[testRow][testCol].equals(value) ? replacedValue : value, defaultSchema[testCol]);

        for (int row = 0; row < table.getRowCount(); row++) {
            String[] expectedRecord = abcTable[row];
            StringRecord actualRecord = table.getRecord(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                String expectedValue = expectedRecord[col];
                if (row == testRow && col == testCol) {
                    expectedValue = replacedValue;
                }
                Assert.assertEquals(message, expectedValue, actualRecord.get(defaultSchema[col]));
            }
        }
    }

    @Test
    public void allRecordsDecoded() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        table.addStringDecoder(value -> abcTable[testRow][testCol].equals(value) ? replacedValue : value, defaultSchema[testCol]);

        List<StringRecord> records = table.getAllRecords();
        for (int row = 0; row < records.size(); row++) {
            String[] expectedRecord = abcTable[row];
            StringRecord actualRecord = records.get(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                String expectedValue = expectedRecord[col];
                if (row == testRow && col == testCol) {
                    expectedValue = replacedValue;
                }
                Assert.assertEquals(message, expectedValue, actualRecord.get(defaultSchema[col]));
            }
        }
    }

    @Test
    public void multipleDecodersAreChained() {
        StringTable table = getAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        table.addStringDecoder(value -> abcTable[testRow][testCol].equals(value) ? replacedValue : value, defaultSchema[testCol]);
        table.addStringDecoder(value -> value.replace("replaced", "new"), defaultSchema[testCol]);

        Assert.assertEquals("new-value", table.getRecord(testRow).get(defaultSchema[testCol]));
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

    @Test(expected = IllegalArgumentException.class)
    public void schemaMustBeUnique() {
        DefaultStringTableImpl.newBuilder(new String[] {"A","B","A"});
    }

    @Test
    public void singleRowTable() {
        StringTable singleRowTable = StringTableFactory.getInstance().getFixWidthParser(new StringReader("A B C ")).parse(new int[] {2, 2, 2});
        Assert.assertEquals("Row count", 1, singleRowTable.getRowCount());
        StringRecord theRecord = singleRowTable.getRecord(0);
        Assert.assertEquals("element 0", "A", theRecord.get("0"));
        Assert.assertEquals("element 1", "B", theRecord.get("1"));
        Assert.assertEquals("element 2", "C", theRecord.get("2"));
    }

}
