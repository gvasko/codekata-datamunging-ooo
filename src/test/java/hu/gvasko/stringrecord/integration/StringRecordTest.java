package hu.gvasko.stringrecord.integration;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.testutils.categories.ComponentLevelTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Check if the classes that are already tested in isolation works together.
 * Created by gvasko on 2015.05.07..
 */
@Category(ComponentLevelTest.class)
public class StringRecordTest {

    @Test
    public void when_GetExistingField_then_ReturnsValue() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        Assert.assertEquals(StringRecordFixtures.SINGLE_VALUE, rec.get(StringRecordFixtures.SINGLE_KEY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_GetMissingField_then_ThrowsException() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        rec.get("surely missing field");
    }

    @Test(expected = NullPointerException.class)
    public void when_GetNullParameter_then_ThrowsNPE() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        rec.get(null);
    }

    @Test
    public void when_Get_then_EveryFieldIsAccessible() {
        StringRecord rec = StringRecordFixtures.getTrioFieldsRecord();
        Assert.assertEquals(StringRecordFixtures.TRIO1_VALUE, rec.get(StringRecordFixtures.TRIO1_KEY));
        Assert.assertEquals(StringRecordFixtures.TRIO2_VALUE, rec.get(StringRecordFixtures.TRIO2_KEY));
        Assert.assertEquals(StringRecordFixtures.TRIO3_VALUE, rec.get(StringRecordFixtures.TRIO3_KEY));
    }

}
