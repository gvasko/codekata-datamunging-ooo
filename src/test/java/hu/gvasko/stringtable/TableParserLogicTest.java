package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

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
        StringTableBuilderFactory stubTableBuilderFactory = mock(StringTableBuilderFactory.class);
        when(stubTableBuilderFactory.createNewRecordBuilder()).thenReturn(stubRecBuilder);
        spyTableBuilder = mock(StringTableBuilder.class);
        when(stubTableBuilderFactory.createNewTableBuilder(anyVararg())).thenReturn(spyTableBuilder);
        sutTableParserLogic = UnitTestFixtures.getTableParserLogic(stubTableBuilderFactory, spyRecParser);
    }

    @Test
    public void when_NoLineToParse_then_CreatesEmptyTable() {
        sutTableParserLogic.getTable();
        verify(spyRecParser, never()).parseRecord(anyString());
    }

    @Test
    public void when_SetFirstRowHeaderTwice_then_SecondInvocationIsIgnored_inOrderTo_avoidIllegalState() {
        sutTableParserLogic.setFirstRowHeader(true);
        Assert.assertTrue("first invocation", sutTableParserLogic.isFirstRowHeader());
        sutTableParserLogic.setFirstRowHeader(false);
        Assert.assertTrue("second invocation", sutTableParserLogic.isFirstRowHeader());
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
