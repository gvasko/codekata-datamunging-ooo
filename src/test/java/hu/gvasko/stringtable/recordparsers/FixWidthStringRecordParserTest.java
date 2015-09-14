package hu.gvasko.stringtable.recordparsers;

import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



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
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns.length)));
        assertThat(recParser.parseRecord(""), is(equalTo(arrayOfEmptyStrings)));
    }

    @Test
    public void when_LastColumnMissing_then_UseEmptyString() {
        final String[] record = {"aaa", "bbbb", ""};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns.length)));
        assertThat(recParser.parseRecord("aaabbbb"), is(equalTo(record)));
    }

    @Test
    public void when_SpacesAreAround_then_FieldValuesAreTrimmed() {
        final String[] record = {"a", "b", "c"};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns.length)));
        assertThat(recParser.parseRecord(" a  b    c  "), is(equalTo(record)));
    }

    @Test
    public void when_LineIsLongerThanExpected_then_TailIsSkipped() {
        final String[] record = {"aaa", "bbbb", "ccccc"};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns.length)));
        assertThat(recParser.parseRecord("aaabbbbcccccdddddd"), is(equalTo(record)));
    }
}
