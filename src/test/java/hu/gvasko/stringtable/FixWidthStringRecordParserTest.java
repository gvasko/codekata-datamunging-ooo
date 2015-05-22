package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created by gvasko on 2015.05.22..
 */
@Category(UnitTest.class)
public class FixWidthStringRecordParserTest {

    int[] testColumns;
    StringRecordParser recParser;

    @Before
    public void setUp() {
        testColumns = new int[] {3, 4, 5};
        recParser = UnitTestFixtures.getFixWidthRecordParser(testColumns);
    }

    @Test
    public void emptyLine_gives_arrayOfEmptyStrings() {
        final String[] arrayOfEmptyStrings = {"", "", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord(""));
    }

    @Test
    public void missingLastColumn_is_emptyString() {
        final String[] arrayOfEmptyStrings = {"aaa", "bbbb", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord("aaabbbb"));
    }

    @Test
    public void fieldValuesAreTrimmed() {
        final String[] arrayOfEmptyStrings = {"a", "b", "c"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord(" a  b    c  "));
    }

    @Test
    public void linesLongerThanExpected_tailIsSkipped() {
        final String[] arrayOfEmptyStrings = {"aaa", "bbbb", "ccccc"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord("aaabbbbcccccdddddd"));
    }
}
