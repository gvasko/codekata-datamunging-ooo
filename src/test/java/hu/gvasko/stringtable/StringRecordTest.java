package hu.gvasko.stringtable;

import org.junit.Assert;
import org.junit.Test;

import static hu.gvasko.stringtable.StringRecordFixtures.*;

/**
 * Created by gvasko on 2015.05.07..
 */
public class StringRecordTest {

    @Test
    public void existingFieldReturnsValue() {
        StringRecord rec = getSingleFieldRecord();
        Assert.assertEquals(SINGLE_VALUE, rec.get(SINGLE_KEY));
    }

    @Test(expected = RuntimeException.class)
    public void missingFieldThrowsRuntimeException() {
        StringRecord rec = getSingleFieldRecord();
        rec.get("surely missing field");
    }

    @Test(expected = NullPointerException.class)
    public void nullParameterThrowsNPE() {
        StringRecord rec = getSingleFieldRecord();
        rec.get(null);
    }

    @Test
    public void everyFieldIsAccessible() {
        StringRecord rec = getTrioFieldsRecord();
        Assert.assertEquals(TRIO1_VALUE, rec.get(TRIO1_KEY));
        Assert.assertEquals(TRIO2_VALUE, rec.get(TRIO2_KEY));
        Assert.assertEquals(TRIO3_VALUE, rec.get(TRIO3_KEY));
    }

    @Test
    public void numberBasedFieldsAreAccessible() {
        StringRecord rec = getNumberBasedRecords();
        Assert.assertEquals(NUMBER0_VALUE, rec.get(NUMBER0_KEY));
        Assert.assertEquals(NUMBER1_VALUE, rec.get(NUMBER1_KEY));
        Assert.assertEquals(NUMBER10_VALUE, rec.get(NUMBER10_KEY));
    }

    @Test(expected = IllegalStateException.class)
    public void emptyRecordCannotBeCreatedThrowsException() {
        DefaultStringRecordImpl.newBuilder().build();
    }

    @Test
    public void toStringReturnsCSV() {
        StringRecord rec = getTrioFieldsRecord();
        Assert.assertEquals(TRIO1_VALUE+','+TRIO2_VALUE+','+TRIO3_VALUE, rec.toString());
    }
}
