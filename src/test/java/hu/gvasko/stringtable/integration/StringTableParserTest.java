package hu.gvasko.stringtable.integration;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableFactory;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import static hu.gvasko.stringtable.filters.StringTableFilters.*;
import hu.gvasko.testutils.categories.ComponentLevelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static hu.gvasko.stringtable.defaultimpl.StringTableParserFixtures.*;

/**
 * Check if the classes that are already tested in isolation works together.
 * Created by Gvasko on 2015.05.08..
 */
@Category(ComponentLevelTest.class)
public class StringTableParserTest {

    private StringTableFactory factory;
    private String[] defaultSchema;

    @Before
    public void setUp() {
        factory = new DefaultStringTableFactoryImpl(new DefaultStringRecordFactoryImpl());
        defaultSchema = getDefaultSchema();
    }

    @Test
    public void parseWith_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(defaultText))) {
            parser.addLineFilter(skipEmptyLines());
            parser.addRecordFilter(onlyNumbersInColumn(defaultSchema[0]));
            StringTable table = parser.firstRowIsHeader().parse();
            assertEachCellIsValid(getTable_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn(), table, defaultSchema);
        }
    }

    @Test
    public void parseWith_skipEmptyLines_skipSplitterLines() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(defaultText))) {
            parser.addLineFilter(skipEmptyLines());
            parser.addLineFilter(skipSplitterLines());
            StringTable table = parser.parse();
            assertEachCellIsValid(getTable_skipEmptyLines_skipSplitterLines(), table, getNumberedSchema());
        }
    }

    @Test
    public void parseWith_skipEmptyLines() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(defaultText))) {
            parser.addLineFilter(skipEmptyLines());
            StringTable table = parser.parse();
            assertEachCellIsValid(getTable_skipEmptyLines(), table, getNumberedSchema());
        }
    }

    @Test
    public void parseWith_fullTable() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(defaultText))) {
            StringTable table = parser.parse();
            assertEachCellIsValid(getTable_full(), table, getNumberedSchema());
        }
    }

    @Test
    public void parseEmptyText() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(emptyText))) {
            StringTable emptyTable = parser.parse();
            assertThat("Row count", emptyTable.getRowCount(), is(equalTo(0)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseEmptyTextIfFirstRowIsHeader() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(emptyText))) {
            parser.firstRowIsHeader().parse();
        }
    }

    @Test
    public void parseSpaces() throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(getDefaultRecordParser(), new StringReader(spaceText))) {
            parser.addLineFilter(skipEmptyLines());
            StringTable emptyTable = parser.parse();
            assertThat("Row count", emptyTable.getRowCount(), is(equalTo(0)));
        }
    }

    private void assertEachCellIsValid(String[][] expected, StringTable actual, String[] schema) {
        assertThat("Row count", actual.getRowCount(), is(equalTo(expected.length)));
        for (int row = 0; row < expected.length; row++) {
            String[] expectedRecord = expected[row];
            StringRecord actualRecord = actual.getRecord(row);
            for (int col = 0; col < defaultSchema.length; col++) {
                String message = "Row " + Integer.toString(row) + ", column " + defaultSchema[col];
                assertThat(message, actualRecord.get(schema[col]), is(equalTo(expectedRecord[col])));
            }
        }
    }
}
