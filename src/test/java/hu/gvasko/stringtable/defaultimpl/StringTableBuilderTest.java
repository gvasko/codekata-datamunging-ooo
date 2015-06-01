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

/**
 * Created by gvasko on 2015.05.23..
 */
@Category(UnitTest.class)
public class StringTableBuilderTest {

    private StringTableBuilder tableBuilder;
    private StringTableFactory spyTableFactory;
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
        spyTableFactory = mock(StringTableFactory.class);
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
        Assert.assertNotEquals("Precondition", "XX", schema[0]);
        Assert.assertNotEquals("Precondition", "XX", tmpSchema[0]);
        tmpSchema[0] = "XX";
        Assert.assertNotEquals("Original schema is not changed", "XX", schema[0]);
        Assert.assertArrayEquals("Original and newly obtained schemas equal", schema, tableBuilder.getSchema());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_ZeroRecordAdded_then_EmptyTableIsCreated() {
        tableBuilder.build();
        ArgumentCaptor<List> recordCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyTableFactory).createNew(aryEq(schema), recordCaptor.capture());
        Assert.assertEquals("Number of record passed", 0, recordCaptor.getValue().size());
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
    public void when_RecordsAdded_then_AllArePassedToTheFactory() {
        tableBuilder.addRecord(rec1);
        tableBuilder.addRecord(rec2);
        tableBuilder.build();

        List<String[]> recordList = getCapturedRecords(1);

        Assert.assertEquals("Number of record passed", 2, recordList.size());
        Assert.assertArrayEquals("First record", rec1, recordList.get(0));
        Assert.assertArrayEquals("Second record", rec2, recordList.get(1));
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

        Assert.assertEquals("Number of record passed", 3, recordList.size());
        Assert.assertArrayEquals("First record", rec1, recordList.get(0));
        Assert.assertArrayEquals("Second record", rec2, recordList.get(1));
        Assert.assertArrayEquals("Third record", rec3, recordList.get(2));
    }

    @SuppressWarnings("unchecked")
    private List<String[]> getCapturedRecords(int callCount) {
        ArgumentCaptor<List> recordCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyTableFactory, times(callCount)).createNew(aryEq(schema), recordCaptor.capture());
        return recordCaptor.getValue();
    }

    private static StringTableBuilder getNewTableBuilder(StringTableFactory spyTableFactory, String[] schema) {
        return new DefaultStringTableBuilderImpl.FactoryImpl(spyTableFactory).createNewTableBuilder(schema);
    }

}
