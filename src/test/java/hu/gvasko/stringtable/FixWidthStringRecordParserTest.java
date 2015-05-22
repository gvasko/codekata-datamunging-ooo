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
    public void given() {
        testColumns = new int[] {3, 4, 5};
        recParser = UnitTestFixtures.getFixWidthRecordParser(testColumns);
    }

    @Test
    public void when_EmptyLineParsed_then_ReturnsArrayOfEmptyStrings() {
        final String[] arrayOfEmptyStrings = {"", "", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord(""));
    }

    @Test
    public void when_LastColumnMissing_then_UseEmptyString() {
        final String[] arrayOfEmptyStrings = {"aaa", "bbbb", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord("aaabbbb"));
    }

    @Test
    public void when_SpacesAreAround_then_FieldValuesAreTrimmed() {
        final String[] arrayOfEmptyStrings = {"a", "b", "c"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord(" a  b    c  "));
    }

    @Test
    public void when_LineIsLongerThanExpected_then_TailIsSkipped() {
        final String[] arrayOfEmptyStrings = {"aaa", "bbbb", "ccccc"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord("aaabbbbcccccdddddd"));
    }
}
