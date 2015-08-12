package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;


/**
 * Created by gvasko on 2015.05.24..
 */
@Category(UnitTest.class)
public class StringTableParserContextTest {

    private static final String LINE1 = "line1";
    private static final String LINE2 = "line2";
    private static final String[] LINES = { LINE1, LINE2 };
    private static final String TEXT = String.join("\n", LINES);

    private StringTableParser sutTableParser;
    private TableParserLogic spyParserLogic;
    private TableParserLogicConstructorDelegate spyTableParserLogicConstructorDelegate;
    private StringRecordParser dummyRecParser;
    private Reader fakeReader;

    private Boolean passedFirstRowIsHeader;
    private List<Predicate<String>> passedLineFilters;
    private List<Predicate<StringRecord>> passedRecordFilters;
    private List<String> passedLines;

    @Before
    @SuppressWarnings("unchecked")
    public void given() {
        spyTableParserLogicConstructorDelegate = mock(TableParserLogicConstructorDelegate.class);
        spyParserLogic = mock(TableParserLogic.class);
        when(spyParserLogic.getTable()).thenReturn(mock(StringTable.class));
        when(spyTableParserLogicConstructorDelegate.call(any(), anyBoolean(), anyList(), anyList())).thenReturn(spyParserLogic);
        dummyRecParser = mock(StringRecordParser.class);
        fakeReader = spy(new StringReader(TEXT));

        sutTableParser = new DefaultTableParserLineReaderImpl.ConstructorDelegateImpl(spyTableParserLogicConstructorDelegate).call(dummyRecParser, fakeReader);

        passedFirstRowIsHeader = null;
    }

    @Test
    public void when_FirstRowIsHeader_then_PassesTrue() {
        sutTableParser.firstRowIsHeader().parse();
        getPassedValuesAfterParse();
        Assert.assertTrue("first row is header", passedFirstRowIsHeader.booleanValue());
    }

    @Test
    public void when_FirstRowIsNotHeader_then_PassesFalse() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertFalse("first row is not header", passedFirstRowIsHeader.booleanValue());
    }

    @Test
    public void when_NoFilterAddedToColumn_then_NothingPassed() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertTrue("no line filters added", passedLineFilters.isEmpty());
        Assert.assertTrue("no record filters added", passedRecordFilters.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddLineFilterToColumn_then_PassesIt() {
        sutTableParser.addLineFilter(mock(Predicate.class));
        sutTableParser.addLineFilter(mock(Predicate.class));
        sutTableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertEquals("line filters added", 2, passedLineFilters.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddRecordFilterToColumn_then_PassesIt() {
        sutTableParser.addRecordFilter(mock(Predicate.class));
        sutTableParser.addRecordFilter(mock(Predicate.class));
        sutTableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertEquals("record filters added", 2, passedRecordFilters.size());
    }

    @Test
    public void when_MultiLineText_then_ParsesEachRow() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertArrayEquals(LINES, passedLines.toArray(new String[0]));
    }

    @Test
    public void when_FinishesParsing_then_TryWithResourcesClosesReader() throws IOException {
        Exception testEx = new RuntimeException("test exception");
        Mockito.doThrow(testEx).when(spyParserLogic).parseRawLine(eq(LINE2));
        try (StringTableParser parser = sutTableParser) {
            parser.parse();
            Assert.fail("Exception should be thrown");
        } catch (Exception e) {
            Assert.assertSame("Expected exception thrown", testEx, e);
            Assert.assertEquals("Number of suppressed exceptions", 0, e.getSuppressed().length);
        }
        verify(fakeReader).close();
    }

    @Test
    public void when_ExceptionDuringParsingAndClosing_then_SuppressedExceptionIsAvailable() throws IOException {
        RuntimeException testEx = new RuntimeException("test exception");
        Mockito.doThrow(testEx).when(spyParserLogic).parseRawLine(eq(LINE2));

        RuntimeException closeEx = new RuntimeException("closing exception");
        Mockito.doThrow(closeEx).when(fakeReader).close();

        try (StringTableParser parser = sutTableParser) {
            parser.parse();
            Assert.fail("Exception should be thrown");
        } catch (Exception e) {
            Assert.assertSame("Expected exception thrown", testEx, e);
            Assert.assertEquals("Number of suppressed exceptions", 1, e.getSuppressed().length);
            Assert.assertSame("Expected suppressed exception", closeEx, e.getSuppressed()[0]);
        }
    }

    @SuppressWarnings("unchecked")
    private void getPassedValuesAfterParse() {
        ArgumentCaptor<Boolean> headerCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> lineFilterCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> recordFilterCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyTableParserLogicConstructorDelegate).call(eq(dummyRecParser), headerCaptor.capture(), lineFilterCaptor.capture(), recordFilterCaptor.capture());
        passedFirstRowIsHeader = headerCaptor.getValue();
        passedLineFilters = lineFilterCaptor.getValue();
        passedRecordFilters = recordFilterCaptor.getValue();

        ArgumentCaptor<String> lineCaptor = ArgumentCaptor.forClass(String.class);
        verify(spyParserLogic, times(LINES.length)).parseRawLine(lineCaptor.capture());
        passedLines = lineCaptor.getAllValues();
    }


}
