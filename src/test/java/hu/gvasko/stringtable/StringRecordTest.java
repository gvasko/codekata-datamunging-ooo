package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gvasko on 2015.05.23..
 */

@Category(UnitTest.class)
public class StringRecordTest {

    private Map<String, String> stringMap;
    private StringRecord rec;

    @Before
    public void given() {
        stringMap = new HashMap<>();
        stringMap.put("AA", "axa");
        stringMap.put("BB", "bxb");
        stringMap.put("CC", "cxc");
        rec = UnitTestFixtures.getStringRecord(stringMap);
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
        Assert.assertEquals("AA", "axa", rec.get("AA"));
        Assert.assertEquals("BB", "bxb", rec.get("BB"));
        Assert.assertEquals("CC", "cxc", rec.get("CC"));
    }

    @Test
    public void when_ToString_then_ContainsAllFieldsAndValues() {
        String str = rec.toString();
        Assert.assertTrue("contains AA", str.contains("AA"));
        Assert.assertTrue("contains BB", str.contains("BB"));
        Assert.assertTrue("contains CC", str.contains("CC"));
        Assert.assertTrue("contains axa", str.contains("axa"));
        Assert.assertTrue("contains bxb", str.contains("bxb"));
        Assert.assertTrue("contains cxc", str.contains("cxc"));
    }
}
