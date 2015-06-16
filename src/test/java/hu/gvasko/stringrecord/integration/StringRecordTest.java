package hu.gvasko.stringrecord.integration;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.testutils.categories.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created by gvasko on 2015.05.07..
 */
@Category(IntegrationTest.class)
public class StringRecordTest {

    @Test
    public void existingFieldReturnsValue() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        Assert.assertEquals(StringRecordFixtures.SINGLE_VALUE, rec.get(StringRecordFixtures.SINGLE_KEY));
    }

    @Test(expected = RuntimeException.class)
    public void missingFieldThrowsRuntimeException() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        rec.get("surely missing field");
    }

    @Test(expected = NullPointerException.class)
    public void nullParameterThrowsNPE() {
        StringRecord rec = StringRecordFixtures.getSingleFieldRecord();
        rec.get(null);
    }

    @Test
    public void everyFieldIsAccessible() {
        StringRecord rec = StringRecordFixtures.getTrioFieldsRecord();
        Assert.assertEquals(StringRecordFixtures.TRIO1_VALUE, rec.get(StringRecordFixtures.TRIO1_KEY));
        Assert.assertEquals(StringRecordFixtures.TRIO2_VALUE, rec.get(StringRecordFixtures.TRIO2_KEY));
        Assert.assertEquals(StringRecordFixtures.TRIO3_VALUE, rec.get(StringRecordFixtures.TRIO3_KEY));
    }

    @Test
    public void numberBasedFieldsAreAccessible() {
        StringRecord rec = StringRecordFixtures.getNumberBasedRecords();
        Assert.assertEquals(StringRecordFixtures.NUMBER0_VALUE, rec.get(StringRecordFixtures.NUMBER0_KEY));
        Assert.assertEquals(StringRecordFixtures.NUMBER1_VALUE, rec.get(StringRecordFixtures.NUMBER1_KEY));
        Assert.assertEquals(StringRecordFixtures.NUMBER10_VALUE, rec.get(StringRecordFixtures.NUMBER10_KEY));
    }

}
