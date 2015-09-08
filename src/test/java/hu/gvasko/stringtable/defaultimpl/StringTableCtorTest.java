package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by gvasko on 2015.05.27..
 */
@Category(UnitTest.class)
public class StringTableCtorTest {

    private String[] fakeSchema;
    private StringRecordFactory spyRecordFactory;

    @Before
    public void given() {
        fakeSchema = new String[] { "AA", "BB" };
        spyRecordFactory = mock(StringRecordFactory.class);
    }

    @Test
    public void when_EmptyRecordList_then_CreatesEmptyTable() {
        List<String[]> fakeRecords = new ArrayList<>();
        StringTable sutTable = getNewStringTable(spyRecordFactory, fakeSchema, fakeRecords);
        assertThat("Number of records", sutTable.getRowCount(), is(equalTo(0)));
        assertThat("Getting all records returns empty list", sutTable.getAllRecords(), is(empty()));
        verifyZeroInteractions(spyRecordFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_LessColumnsProvidedThanSpecifiedByTheSchema_then_ThrowsException() {
        List<String[]> fakeRecords = Arrays.asList(new String[][]{
                {"a1", "b1"},
                {"a2"}
        });
        getNewStringTable(spyRecordFactory, fakeSchema, fakeRecords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_MoreColumnsProvidedThanSpecifiedByTheSchema_then_ThrowsException() {
        List<String[]> fakeRecords = Arrays.asList(new String[][]{
                {"a1", "b1"},
                {"a2", "b2", "c2"}
        });
        getNewStringTable(spyRecordFactory, fakeSchema, fakeRecords);
    }

    private static StringTable getNewStringTable(StringRecordFactory spyRecordFactory, String[] fakeSchema, List<String[]> fakeRecords) {
        return new DefaultStringTableImpl(spyRecordFactory, fakeSchema, fakeRecords);
    }


}
