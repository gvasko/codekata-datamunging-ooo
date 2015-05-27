package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

/**
 * Created by gvasko on 2015.05.26..
 */
@Category(UnitTest.class)
public class StringTableTest {

    private StringTable sutTable;
    private StringRecordBuilderFactory spyRecBuilderFactory;
    private String[] fakeSchema;
    private List<String[]> fakeRecords;

    @Before
    public void given() {

    }

    @Test
    public void when_EmptyRecordList_then_CreatesEmptyTable() {

        Assert.assertEquals("Number of records", 0, sutTable.getRowCount());
        Assert.assertTrue("Getting all records returns empty list", sutTable.getAllRecords().isEmpty());
    }

    @Test
    public void when_GetRowCount_then_ReturnsNumberOfGivenRecords() {
        Assert.fail();
    }

    @Test
    public void when_DecoderAddedToNoColumn_then_ThrowsException() {
        Assert.fail();
    }

    @Test
    public void when_DecoderAddedToAColumn_then_AppliesItWhenGetRecord() {
        Assert.fail();

    }

    @Test
    public void when_DecoderAddedToAColumn_then_AppliesItWhenGetAllRecords() {
        Assert.fail();

    }

    @Test
    public void when_MultipleDecoderAddedToTheSameColumn_then_JoinsThemWhenApplying() {
        Assert.fail();

    }

    @Test
    public void when_GetRecordOutOfBounds_then_ThrowsException() {
        Assert.fail();

    }

}
