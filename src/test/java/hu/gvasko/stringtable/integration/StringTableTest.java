package hu.gvasko.stringtable.integration;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import hu.gvasko.testutils.categories.ComponentLevelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static hu.gvasko.stringtable.integration.StringTableFixtures.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Check if the classes that are already tested in isolation works together.
 * Created by gvasko on 2015.05.07..
 */
@Category(ComponentLevelTest.class)
public class StringTableTest {

    interface RecordSupplier {
        StringRecord getRecordAtRow(int row);
    }

    interface RawRecordSupplier {
        String[] getRecordAtRow(int row);
    }

    private String[] defaultSchema;
    private String[][] abcTable;

    @Before
    public void setUp() {
        defaultSchema = getDefaultSchema();
        abcTable = getAbcArrays();
    }

    @Test
    public void emptyTableReturnsEmptyList() {
        StringTable table = createEmptyTable();
        assertThat(table.getAllRecords().toArray(), is(equalTo(new StringRecord[0])));
    }

    @Test
    public void returnsRecordAtIndex() {
        StringTable table = createAbcTable();
        assertThat("Row count", table.getRowCount(), is(equalTo(abcTable.length)));
        assertThatRowsAreEqual(table, row -> abcTable[row], row -> table.getRecord(row));
    }

    @Test
    public void returnsAllRecords() {
        StringTable table = createAbcTable();
        List<StringRecord> records = table.getAllRecords();
        assertThat("Record count", records, hasSize(abcTable.length));
        assertThatRowsAreEqual(table, row -> abcTable[row], row -> records.get(row));
    }

    @Test
    public void recordAtIndexDecoded() {
        StringTable table = createAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        final String testValue = abcTable[testRow][testCol];
        table.addStringDecoderToColumns(value -> testValue.equals(value) ? replacedValue : value, defaultSchema[testCol]);

        assertThatRowsAreEqual(table, row -> row != testRow ? abcTable[row] : copyAndReplace(abcTable[row], testCol, replacedValue), row -> table.getRecord(row));
    }

    @Test
    public void allRecordsDecoded() {
        StringTable table = createAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        final String testValue = abcTable[testRow][testCol];
        table.addStringDecoderToColumns(value -> testValue.equals(value) ? replacedValue : value, defaultSchema[testCol]);

        List<StringRecord> records = table.getAllRecords();
        assertThatRowsAreEqual(table, row -> row != testRow ? abcTable[row] : copyAndReplace(abcTable[row], testCol, replacedValue), row -> records.get(row));
    }

    private static String[] copyAndReplace(String[] rawRecord, int col, String newValue) {
        String[] tmp = Arrays.copyOf(rawRecord, rawRecord.length);
        tmp[col] = newValue;
        return tmp;
    }

    private void assertThatRowsAreEqual(StringTable table, RawRecordSupplier expectedRecSupplier, RecordSupplier actualRecSupplier) {
        for (int row = 0; row < table.getRowCount(); row++) {
            String[] expectedRecord = expectedRecSupplier.getRecordAtRow(row);
            StringRecord actualRecord = actualRecSupplier.getRecordAtRow(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = getMessage(row, col);
                assertThat(message, actualRecord.get(defaultSchema[col]), is(equalTo(expectedRecord[col])));
            }
        }
    }

    private String getMessage(int row, int col) {
        return "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
    }

    @Test
    public void multipleDecodersAreChained() {
        StringTable table = createAbcTable();
        final String replacedValue = "replaced-value";
        final int testRow = 2;
        final int testCol = BBB_COLUMN;
        table.addStringDecoderToColumns(value -> abcTable[testRow][testCol].equals(value) ? replacedValue : value, defaultSchema[testCol]);
        table.addStringDecoderToColumns(value -> value.replace("replaced", "new"), defaultSchema[testCol]);

        assertThat(table.getRecord(testRow).get(defaultSchema[testCol]), is(equalTo("new-value")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assigningDecoderToUndefinedFieldThrowsException() {
        StringTable table = createAbcTable();
        table.addStringDecoderToColumns(value -> "", "invalid-column");
    }

    @Test
    public void addingDecoderToEmptyTableIsAllowed() {
        StringTable table = createEmptyTable();
        table.addStringDecoderToColumns(value -> "", defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderThrowsNPE() {
        StringTable table = createAbcTable();
        table.addStringDecoderToColumns(null, defaultSchema[AAA_COLUMN]);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderToInvalidColumnThrowsNPE() {
        StringTable table = createAbcTable();
        table.addStringDecoderToColumns(null, "invalid-column");
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDecoderWithoutColumnThrowsNPE() {
        StringTable table = createAbcTable();
        table.addStringDecoderToColumns(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingDecoderWithoutColumnThrowsException() {
        StringTable table = createAbcTable();
        table.addStringDecoderToColumns(value -> "");
    }

    @Test
    public void singleRowTable() {
        StringTableParser tableParser = new DefaultStringTableFactoryImpl(new DefaultStringRecordFactoryImpl()).createStringTableParser(new FixWidthTextParserImpl(2, 2, 2), new StringReader("A B C "));
        StringTable singleRowTable = tableParser.parse();
        assertThat("Row count", singleRowTable.getRowCount(), is(equalTo(1)));
        StringRecord theRecord = singleRowTable.getRecord(0);
        assertThat("element 0", theRecord.get("0"), is(equalTo("A")));
        assertThat("element 1", theRecord.get("1"), is(equalTo("B")));
        assertThat("element 2", theRecord.get("2"), is(equalTo("C")));
    }

}
