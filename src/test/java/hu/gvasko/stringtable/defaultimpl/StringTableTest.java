package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.mockito.Mockito.*;


/**
 * Created by gvasko on 2015.05.26..
 */
@Category(UnitTest.class)
public class StringTableTest {

    private StringTable sutTable;
    private StringRecordBuilder spyRecBuilder;
    private StringRecordBuilderConstructorDelegate spyRecBuilderCtor;
    private String[] fakeSchema;
    private List<String[]> fakeRecords;
    private String[] passedFields;
    private String[] passedValues;

    @Before
    public void given() {
        fakeSchema = new String[] { "AA", "BB" };
        fakeRecords = Arrays.asList(new String[][] {
                { "a1", "b1" },
                { "a2", "b2" }
        });

        spyRecBuilder = mock(StringRecordBuilder.class);
        when(spyRecBuilder.build()).thenReturn(
                mock(StringRecord.class),
                mock(StringRecord.class)
        );

        spyRecBuilderCtor = mock(StringRecordBuilderConstructorDelegate.class);
        when(spyRecBuilderCtor.call()).thenReturn(spyRecBuilder);

        sutTable = new DefaultStringTableImpl.ConstructorDelegateImpl(spyRecBuilderCtor).call(fakeSchema, fakeRecords);
    }

    @Test
    public void when_GetRowCount_then_ReturnsNumberOfGivenRecords() {
        Assert.assertEquals("row count", fakeRecords.size(), sutTable.getRowCount());
    }

    @Test
    public void when_GetAllRecords_then_ReturnsAllGivenRecords() {
        Assert.assertEquals("number of all records", fakeRecords.size(), sutTable.getAllRecords().size());
        getPassedRecords();
        Assert.assertArrayEquals("Fields passed", new String[]{"AA", "BB", "AA", "BB"}, passedFields);
        Assert.assertArrayEquals("Values passed", new String[]{"a1", "b1", "a2", "b2"}, passedValues);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void when_DecoderAddedToNoColumn_then_ThrowsException() {
        UnaryOperator<String> dummyOp = mock(UnaryOperator.class);
        sutTable.addStringDecoderToColumns(dummyOp);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void when_DecoderAddedToColumnThatDoesNotExist_then_ThrowsException() {
        UnaryOperator<String> dummyOp = mock(UnaryOperator.class);
        sutTable.addStringDecoderToColumns(dummyOp, "XYZ");
    }

    @Test
    public void when_DecoderAddedToAColumn_then_AppliesItWhenGetRecord() {
        sutTable.addStringDecoderToColumns(getFakeDecoder1(), "AA");
        for (int i = 0; i < sutTable.getRowCount(); i++) {
            sutTable.getRecord(i);
        }
        getPassedRecords();
        Assert.assertArrayEquals("Fields passed", new String[]{"AA", "BB", "AA", "BB"}, passedFields);
        Assert.assertArrayEquals("Values passed", new String[]{"aa11", "b1", "aa22", "b2"}, passedValues);
    }

    @Test
    public void when_DecoderAddedToAColumn_then_AppliesItWhenGetAllRecords() {
        sutTable.addStringDecoderToColumns(getFakeDecoder1(), "AA");
        sutTable.getAllRecords();
        getPassedRecords();
        Assert.assertArrayEquals("Fields passed", new String[]{"AA", "BB", "AA", "BB"}, passedFields);
        Assert.assertArrayEquals("Values passed", new String[]{"aa11", "b1", "aa22", "b2"}, passedValues);
    }

    @Test
    public void when_MultipleDecoderAddedToTheSameColumn_then_JoinsThemWhenApplying() {
        sutTable.addStringDecoderToColumns(getFakeDecoder1(), "AA");
        sutTable.addStringDecoderToColumns(getFakeDecoder2(), "AA");
        sutTable.getAllRecords();
        getPassedRecords();
        Assert.assertArrayEquals("Fields passed", new String[]{"AA", "BB", "AA", "BB"}, passedFields);
        Assert.assertArrayEquals("Values passed", new String[]{"abcdefg", "b1", "aa22", "b2"}, passedValues);
    }

    @Test(expected = NullPointerException.class)
    public void when_NullDecoderAdded_then_ThrowsException() {
        sutTable.addStringDecoderToColumns(null, "AA");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void when_GetRecordOutOfBounds_then_ThrowsException() throws IndexOutOfBoundsException {
        sutTable.getRecord(fakeRecords.size());
    }

    private void getPassedRecords() {
        ArgumentCaptor<String> fieldCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        int invocations = fakeSchema.length * fakeRecords.size();
        verify(spyRecBuilder, times(invocations)).addField(fieldCaptor.capture(), valueCaptor.capture());
        passedFields = fieldCaptor.getAllValues().toArray(new String[invocations]);
        passedValues = valueCaptor.getAllValues().toArray(new String[invocations]);
    }

    @SuppressWarnings("unchecked")
    private UnaryOperator<String> getFakeDecoder1() {
        return s -> "a1".equals(s) ? "aa11" : "a2".equals(s) ? "aa22" : s;
    }

    @SuppressWarnings("unchecked")
    private UnaryOperator<String> getFakeDecoder2() {
        return s -> "aa11".equals(s) ? "abcdefg" : s;
    }


}
