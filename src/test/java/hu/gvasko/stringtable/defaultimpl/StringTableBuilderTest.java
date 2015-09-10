package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringtable.StringTableBuilder;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by gvasko on 2015.05.23..
 */
@Category(UnitTest.class)
public class StringTableBuilderTest {

    private StringTableBuilder tableBuilder;
    private StringTableFactoryExt spyTableFactory;
    private String[] schema;

    private final String[] rec1 = new String[] {"a1", "b1", "c1"};
    private final String[] rec2 = new String[] {"a2", "b2", "c2"};
    private final String[] rec3 = new String[] {"a3", "b3", "c3"};

    @Before
    public void given() {
        // TODO: why spy() cannot be used here?
        // http://xunitpatterns.com/Test%20Spy.html
        // "capture the indirect output calls made to another component
        // by the system under test (SUT) for later verification by the test"
        spyTableFactory = mock(StringTableFactoryExt.class);
        schema = new String[] {"AA", "BB", "CC"};
        tableBuilder = getNewTableBuilder(spyTableFactory, schema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_SchemaContainsDuplicate_then_CtorThrowsException() {
        String[] duplicatedSchema = {"AA", "BB", "BB"};
        getNewTableBuilder(spyTableFactory, duplicatedSchema);
    }

    @Test
    public void when_SchemaReturns_then_CopyReturns() {
        String[] tmpSchema = tableBuilder.getSchema();
        assertThat("Precondition", schema[0], is(not(equalTo("XX"))));
        assertThat("Precondition", tmpSchema[0], is(not(equalTo("XX"))));
        tmpSchema[0] = "XX";
        assertThat("Original schema is not changed", schema[0], is(not(equalTo("XX"))));
        assertThat("Original schema is not changed", tableBuilder.getSchema(), is(equalTo(schema)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_ZeroRecordAdded_then_EmptyTableIsCreated() {
        tableBuilder.build();
        List<String[]> recordList = getCapturedRecords(1);
        assertThat("Number of record passed", recordList, is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_LessFieldsAdded_then_ThrowsException() {
        tableBuilder.addRecord("aa", "bb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_MoreFieldsAdded_then_ThrowsException() {
        tableBuilder.addRecord("aa", "bb", "cc", "dd");
    }

    @Test
    public void when_RecordsAdded_then_AllArePassedToTheCtor() {
        tableBuilder.addRecord(rec1);
        tableBuilder.addRecord(rec2);
        tableBuilder.build();

        List<String[]> recordList = getCapturedRecords(1);

        assertThat("Number of record passed", recordList, hasSize(2));
        assertThat("First record passed", recordList.get(0), is(equalTo(rec1)));
        assertThat("Second record passed", recordList.get(1), is(equalTo(rec2)));
    }

    private void andGiven_RecordsAddedAndTableAlreadyBuilt() {
        tableBuilder.addRecord(rec1);
        tableBuilder.addRecord(rec2);
        tableBuilder.build();
    }

    @Test
    public void when_RecordsAddedAfterAlreadyBuilt_then_AllPreviouslyAndNewlyAddedRecordsPass() {
        andGiven_RecordsAddedAndTableAlreadyBuilt();
        tableBuilder.addRecord(rec3);
        tableBuilder.build();

        List<String[]> recordList = getCapturedRecords(2);

        assertThat("Number of record passed", recordList.size(), is(equalTo(3)));
        assertThat("First record passed", recordList.get(0), is(equalTo(rec1)));
        assertThat("Second record passed", recordList.get(1), is(equalTo(rec2)));
        assertThat("Third record passed", recordList.get(2), is(equalTo(rec3)));
    }

    @SuppressWarnings("unchecked")
    private List<String[]> getCapturedRecords(int callCount) {
        ArgumentCaptor<List> recordCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyTableFactory, times(callCount)).createStringTable(aryEq(schema), recordCaptor.capture());
        return recordCaptor.getValue();
    }

    private static StringTableBuilder getNewTableBuilder(StringTableFactoryExt spyTableFactory, String[] schema) {
        return new DefaultStringTableBuilderImpl(spyTableFactory, schema);
    }

}
