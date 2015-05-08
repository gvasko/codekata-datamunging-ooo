package hu.gvasko.stringtable;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import static hu.gvasko.stringtable.StringTableParserFixtures.*;

/**
 * Created by Gvasko on 2015.05.08..
 */
public class StringTableParserTest {

    @Test
    public void parseWith_firstRowIsHeader_excludeLastRow_excludeEmptyRows() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(defaultText))) {
            StringTable table = parser.firstRowIsHeader().excludeLastRow().excludeEmptyRows().parse(defaultHeader);
            assertEachCellIsValid(table_firstRowIsHeader_excludeLastRow_excludeEmptyRows, table, defaultSchema);
        }
    }

    @Test
    public void parseWith_excludeLastRow_excludeEmptyRows() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(defaultText))) {
            StringTable table = parser.excludeLastRow().excludeEmptyRows().parse(defaultHeader);
            assertEachCellIsValid(table_excludeLastRow_excludeEmptyRows, table, numberedSchema);
        }
    }

    @Test
    public void parseWith_excludeEmptyRows() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(defaultText))) {
            StringTable table = parser.excludeEmptyRows().parse(defaultHeader);
            assertEachCellIsValid(table_excludeEmptyRows, table, numberedSchema);
        }
    }

    @Test
    public void parseWith_fullTable() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(defaultText))) {
            StringTable table = parser.parse(defaultHeader);
            assertEachCellIsValid(table_full, table, numberedSchema);
        }
    }

    @Test
    public void parseEmptyText() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(emptyText))) {
            StringTable emptyTable = parser.parse(defaultHeader);
            Assert.assertEquals("Row count: ", 0, emptyTable.getRowCount());
        }
    }

    @Test
    public void parseSpaces() throws Exception {
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(new StringReader(spaceText))) {
            StringTable emptyTable = parser.excludeEmptyRows().parse(defaultHeader);
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
