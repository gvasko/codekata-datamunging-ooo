package hu.gvasko.stringrecord.integration;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.testutils.categories.ComponentLevelTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static hu.gvasko.stringrecord.integration.StringRecordFixtures.*;

/**
 * Check if the classes that are already tested in isolation works together.
 * Created by gvasko on 2015.05.07..
 */
@Category(ComponentLevelTest.class)
public class StringRecordTest {

    @Test
    public void when_GetExistingField_then_ReturnsValue() {
        StringRecord rec = getSingleFieldRecord();
        assertThat(rec.get(SINGLE_KEY), is(equalTo(SINGLE_VALUE)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_GetMissingField_then_ThrowsException() {
        StringRecord rec = getSingleFieldRecord();
        rec.get("surely missing field");
    }

    @Test(expected = NullPointerException.class)
    public void when_GetNullParameter_then_ThrowsNPE() {
        StringRecord rec = getSingleFieldRecord();
        rec.get(null);
    }

    @Test
    public void when_Get_then_EveryFieldIsAccessible() {
        StringRecord rec = getTrioFieldsRecord();
        assertThat(rec.get(TRIO1_KEY), is(equalTo(TRIO1_VALUE)));
        assertThat(rec.get(TRIO2_KEY), is(equalTo(TRIO2_VALUE)));
        assertThat(rec.get(TRIO3_KEY), is(equalTo(TRIO3_VALUE)));
    }

}
