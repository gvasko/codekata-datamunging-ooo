package hu.gvasko.stringtable.recordparsers;

import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.testutils.categories.ClassLevelTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by gvasko on 2015.05.27..
 */
@Category(ClassLevelTest.class)
public class CSVParserTest {

    private static final int testColumns = 3;
    private StringRecordParser recParser;

    @Before
    public void given() {
        recParser = new CSVParserImpl(testColumns);
    }

    @Test
    public void when_EmptyLineParsed_then_ReturnsArrayOfEmptyStrings() {
        final String[] arrayOfEmptyStrings = {"", "", ""};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns)));
        assertThat(recParser.parseRecord(",,"), is(equalTo(arrayOfEmptyStrings)));
    }

    @Test
    public void when_LastColumnMissing_then_UseEmptyString() {
        final String[] record = {"aaa", "bbbb", ""};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns)));
        assertThat(recParser.parseRecord("aaa,bbbb,"), is(equalTo(record)));
    }

    @Test
    public void when_SpacesAreAround_then_FieldValuesAreTrimmed() {
        final String[] record = {"a", "b", "c"};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns)));
        assertThat(recParser.parseRecord(" a , b  ,  c  "), is(equalTo(record)));
    }

    @Test
    public void when_LineIsLongerThanExpected_then_TailIsSkipped() {
        final String[] record = {"aaa", "bbbb", "ccccc"};
        assertThat("Column count", recParser.getColumnCount(), is(equalTo(testColumns)));
        assertThat(recParser.parseRecord("aaa,bbbb,ccccc,dddddd"), is(equalTo(record)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_LineIsShorterThanExpected_then_ThrowsException() {
        recParser.parseRecord("aaa,bbbb");
    }


}
