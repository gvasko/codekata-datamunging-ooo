package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.testutils.categories.ClassLevelTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by gvasko on 2015.05.23..
 */

@Category(ClassLevelTest.class)
public class StringRecordTest {

    private Map<String, String> stringMap;
    private StringRecord rec;

    @Before
    public void given_aStringRecord() {
        stringMap = new HashMap<>();
        stringMap.put("AA", "axa");
        stringMap.put("BB", "bxb");
        stringMap.put("CC", "cxc");
        rec = new DefaultStringRecordImpl(stringMap);
    }

    @Test(expected = NullPointerException.class)
    public void when_GetNullField_then_ThrowNPE() {
        rec.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_GetFieldThatDoesNotExist_then_ThrowException() {
        rec.get("XX");
    }

    @Test
    public void when_GetAllFields_then_AllValuesReturn() {
        assertThat("AA", rec.get("AA"), is(equalTo("axa")));
        assertThat("BB", rec.get("BB"), is(equalTo("bxb")));
        assertThat("CC", rec.get("CC"), is(equalTo("cxc")));
    }

    @Test
    public void when_ToString_then_ContainsAllFieldsAndValues() {
        String str = rec.toString();
        assertThat(str, containsString("AA"));
        assertThat(str, containsString("BB"));
        assertThat(str, containsString("CC"));
        assertThat(str, containsString("axa"));
        assertThat(str, containsString("bxb"));
        assertThat(str, containsString("cxc"));
    }
}
