package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;
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

/**
 * Created by gvasko on 2015.05.27..
 */
@Category(UnitTest.class)
public class StringTableCtorTest {

    private String[] fakeSchema;
    private StringRecordBuilderConstructorDelegate spyRecBuilderCtor;

    @Before
    public void given() {
        fakeSchema = new String[] { "AA", "BB" };
        spyRecBuilderCtor = mock(StringRecordBuilderConstructorDelegate.class);
    }

    @Test
    public void when_EmptyRecordList_then_CreatesEmptyTable() {
        List<String[]> fakeRecords = new ArrayList<>();
        StringTable sutTable = getNewStringTable(spyRecBuilderCtor, fakeSchema, fakeRecords);
        Assert.assertEquals("Number of records", 0, sutTable.getRowCount());
        Assert.assertTrue("Getting all records returns empty list", sutTable.getAllRecords().isEmpty());
        verifyZeroInteractions(spyRecBuilderCtor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_LessColumnsProvidedThanSpecifiedByTheSchema_then_ThrowsException() {
        List<String[]> fakeRecords = Arrays.asList(new String[][]{
                {"a1", "b1"},
                {"a2"}
        });
        getNewStringTable(spyRecBuilderCtor, fakeSchema, fakeRecords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_MoreColumnsProvidedThanSpecifiedByTheSchema_then_ThrowsException() {
        List<String[]> fakeRecords = Arrays.asList(new String[][]{
                {"a1", "b1"},
                {"a2", "b2", "c2"}
        });
        getNewStringTable(spyRecBuilderCtor, fakeSchema, fakeRecords);
    }

    private static StringTable getNewStringTable(StringRecordBuilderConstructorDelegate spyRecBuilderCtor, String[] fakeSchema, List<String[]> fakeRecords) {
        return new DefaultStringTableImpl.ConstructorDelegateImpl(spyRecBuilderCtor).call(fakeSchema, fakeRecords);
    }


}
