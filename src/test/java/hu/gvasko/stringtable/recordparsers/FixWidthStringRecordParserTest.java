package hu.gvasko.stringtable.recordparsers;

import hu.gvasko.stringtable.StringRecordParser;
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
        recParser = new FixWidthTextParserImpl(testColumns);
    }

    @Test
    public void when_EmptyLineParsed_then_ReturnsArrayOfEmptyStrings() {
        final String[] arrayOfEmptyStrings = {"", "", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(arrayOfEmptyStrings, recParser.parseRecord(""));
    }

    @Test
    public void when_LastColumnMissing_then_UseEmptyString() {
        final String[] record = {"aaa", "bbbb", ""};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(record, recParser.parseRecord("aaabbbb"));
    }

    @Test
    public void when_SpacesAreAround_then_FieldValuesAreTrimmed() {
        final String[] record = {"a", "b", "c"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(record, recParser.parseRecord(" a  b    c  "));
    }

    @Test
    public void when_LineIsLongerThanExpected_then_TailIsSkipped() {
        final String[] record = {"aaa", "bbbb", "ccccc"};
        Assert.assertEquals("columnCount", testColumns.length, recParser.getColumnCount());
        Assert.assertArrayEquals(record, recParser.parseRecord("aaabbbbcccccdddddd"));
    }
}
