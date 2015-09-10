package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTableBuilder;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Created by gvasko on 2015.05.27..
 */
@Category(UnitTest.class)
public class TableParserLogicTest {

    private TableParserLogic sutTableParserLogic;
    private StringTableBuilder spyTableBuilder;
    private StringRecordParser spyRecParser;

    @Before
    public void given() {
        spyRecParser = mock(StringRecordParser.class);
        when(spyRecParser.parseRecord(anyString())).thenReturn(new String[] {"x", "y", "z"});

        StringRecordBuilder stubRecBuilder = mock(StringRecordBuilder.class);
        when(stubRecBuilder.addFields(anyVararg(), anyVararg())).thenReturn(stubRecBuilder);
        when(stubRecBuilder.build()).thenReturn(mock(StringRecord.class));

        StringRecordFactory stubRecordFactory = mock(StringRecordFactory.class);
        when(stubRecordFactory.createStringRecordBuilder()).thenReturn(stubRecBuilder);

        StringTableFactoryExt stubTableFactory = mock(StringTableFactoryExt.class);
        when(stubTableFactory.getRecordFactory()).thenReturn(stubRecordFactory);
        spyTableBuilder = mock(StringTableBuilder.class);
        when(stubTableFactory.createStringTableBuilder(anyVararg())).thenReturn(spyTableBuilder);
        sutTableParserLogic = new DefaultTableParserLogicImpl(stubTableFactory, spyRecParser);
    }

    @Test
    public void when_SetFirstRowHeaderTwice_then_SecondInvocationIsIgnored_inOrderTo_avoidIllegalState() {
        sutTableParserLogic.setFirstRowHeader(true);
        assertThat("First invocation", sutTableParserLogic.isFirstRowHeader(), is(true));
        sutTableParserLogic.setFirstRowHeader(false);
        assertThat("Second invocation", sutTableParserLogic.isFirstRowHeader(), is(true));
    }

    @Test
    public void when_FirstRowIsHeader_then_FirstRowIsNotAddedAsRecord() {
        sutTableParserLogic.setFirstRowHeader(true);
        String[] text = andGiven_ABCTable();
        parseText(text);
        int expectedRecordCount = text.length - 1;
        verify(spyTableBuilder, times(expectedRecordCount)).addRecord(anyVararg());
    }

    @Test
    public void when_FirstRowIsNotHeader_then_FirstRowIsAddedAsRecord() {
        sutTableParserLogic.setFirstRowHeader(false);
        String[] text = andGiven_ABCTable();
        parseText(text);
        int expectedRecordCount = text.length;
        verify(spyTableBuilder, times(expectedRecordCount)).addRecord(anyVararg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_FirstRowIsHeader_and_NoRows_then_ThrowsException() {
        sutTableParserLogic.setFirstRowHeader(true);
        sutTableParserLogic.getTable();
    }

    @Test
    public void when_FirstRowIsNotHeader_and_NoRows_then_ReturnsEmptyTable() {
        sutTableParserLogic.setFirstRowHeader(false);
        sutTableParserLogic.getTable();
        verify(spyTableBuilder, never()).addRecord(anyVararg());
        verify(spyRecParser, never()).parseRecord(anyString());
    }

    @Test
    public void when_NotExplicitlySet_then_FirstRowIsNotHeader() {
        assertThat("First row is not header by default", sutTableParserLogic.isFirstRowHeader(), is(false));
    }

    @Test
    public void when_LineFilters_then_LinesOmitted() {
        String[] text = andGiven_ABCTable();
        sutTableParserLogic.addLineFilter(line -> !"ghi".equals(line));
        parseText(text);
        int expectedRecordCount = text.length - 1;
        verify(spyTableBuilder, times(expectedRecordCount)).addRecord(anyVararg());
    }

//    @Test
//    public void when_RecordFilters_then_RecordsOmitted() {
//        // TODO: how to do?
//    }
//
//    @Test
//    public void when_BothFilters_then_LineFiltersFirst() {
//        // TODO: depends on the previous
//    }
//
//  when_HeaderHadDuplicateValues_then_EnsuresUniqueValues





    private String[] andGiven_ABCTable() {
        return new String[] {
                "ABC",
                "def",
                "ghi",
                "jkl",
                "mno"
        };
    }

    private void parseText(String[] text) {
        for (String line : text) {
            sutTableParserLogic.parseRawLine(line);
        }
    }


}
