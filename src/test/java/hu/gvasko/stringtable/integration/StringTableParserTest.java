package hu.gvasko.stringtable.integration;

import hu.gvasko.stringtable.StringRecord;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableFactory;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.testutils.categories.IntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringReader;

import static hu.gvasko.stringtable.StringTableParserFixtures.*;

/**
 * Created by Gvasko on 2015.05.08..
 */
@Category(IntegrationTest.class)
public class StringTableParserTest {

    private StringTableFactory factory;

    @Before
    public void setUp() {
        factory = StringTableFactory.getInstance();
    }

    @Test
    public void parseWith_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(defaultText))) {
            parser.addLineFilter(factory.skipEmptyLines());
            parser.addRecordFilter(factory.onlyNumbersInColumn(defaultSchema[0]));
            StringTable table = parser.firstRowIsHeader().parse();
            assertEachCellIsValid(table_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn, table, defaultSchema);
        }
    }

    @Test
    public void parseWith_skipEmptyLines_skipSplitterLines() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(defaultText))) {
            parser.addLineFilter(factory.skipEmptyLines());
            parser.addLineFilter(factory.skipSplitterLines());
            StringTable table = parser.parse();
            assertEachCellIsValid(table_skipEmptyLines_skipSplitterLines, table, numberedSchema);
        }
    }

    @Test
    public void parseWith_skipEmptyLines() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(defaultText))) {
            parser.addLineFilter(factory.skipEmptyLines());
            StringTable table = parser.parse();
            assertEachCellIsValid(table_skipEmptyLines, table, numberedSchema);
        }
    }

    @Test
    public void parseWith_fullTable() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(defaultText))) {
            StringTable table = parser.parse();
            assertEachCellIsValid(table_full, table, numberedSchema);
        }
    }

    @Test
    public void parseEmptyText() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(emptyText))) {
            StringTable emptyTable = parser.parse();
            Assert.assertEquals("Row count: ", 0, emptyTable.getRowCount());
        }
    }

    @Test
    public void parseSpaces() throws Exception {
        try (StringTableParser parser = factory.newStringTableParser(defaultRecordParser, new StringReader(spaceText))) {
            parser.addLineFilter(factory.skipEmptyLines());
            StringTable emptyTable = parser.parse();
            Assert.assertEquals("Row count: ", 0, emptyTable.getRowCount());
        }
    }

    private void assertEachCellIsValid(String[][] expected, StringTable actual, String[] schema) {
        Assert.assertEquals("Row count: ", expected.length, actual.getRowCount());
        for (int row = 0; row < expected.length; row++) {
            String[] expectedRecord = expected[row];
            StringRecord actualRecord = actual.getRecord(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                Assert.assertEquals(message, expectedRecord[col], actualRecord.get(schema[col]));
            }
        }
    }
}