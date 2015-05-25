package hu.gvasko.stringtable;

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
//import static org.mockito.AdditionalMatchers.*;


/**
 * Created by gvasko on 2015.05.24..
 */
@Category(UnitTest.class)
public class StringTableParserContextTest {

    private static final String LINE1 = "line1";
    private static final String LINE2 = "line2";
    private static final String[] LINES = { LINE1, LINE2 };
    private static final String TEXT = String.join("\n", LINES);

    private StringTableParser tableParser;
    private TableParserLogic mockedParserLogic;
    private TableParserLogicFactory spyTableParserLogicFactory;
    private StringRecordParser spyRecParser;
    private Reader spyReader;

    private Boolean passedFirstRowIsHeader;
    private List<Predicate<String>> passedLineFilters;
    private List<Predicate<StringRecord>> passedRecordFilters;
    private List<String> passedLines;

    @Before
    @SuppressWarnings("unchecked")
    public void given() {
        spyTableParserLogicFactory = mock(TableParserLogicFactory.class);
        mockedParserLogic = mock(TableParserLogic.class);
        when(mockedParserLogic.getTableBuilder()).thenReturn(mock(StringTableBuilder.class));
        when(spyTableParserLogicFactory.createNew(any(), anyBoolean(), anyList(), anyList())).thenReturn(mockedParserLogic);
        spyRecParser = mock(StringRecordParser.class);
        spyReader = spy(new StringReader(TEXT));

        tableParser = UnitTestFixtures.getStringTableParser(spyTableParserLogicFactory, spyRecParser, spyReader);

        passedFirstRowIsHeader = null;
    }

    @Test
    public void when_FirstRowIsHeader_then_PassesTrue() {
        tableParser.firstRowIsHeader().parse();
        getPassedValuesAfterParse();
        Assert.assertTrue("first row is header", passedFirstRowIsHeader.booleanValue());
    }

    @Test
    public void when_FirstRowIsNotHeader_then_PassesFalse() {
        tableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertFalse("first row is not header", passedFirstRowIsHeader.booleanValue());
    }

    @Test
    public void when_NoFilterAddedToColumn_then_NothingPassed() {
        tableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertTrue("no line filters added", passedLineFilters.isEmpty());
        Assert.assertTrue("no record filters added", passedRecordFilters.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddLineFilterToColumn_then_PassesIt() {
        tableParser.addLineFilter(mock(Predicate.class));
        tableParser.addLineFilter(mock(Predicate.class));
        tableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertEquals("line filters added", 2, passedLineFilters.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddRecordFilterToColumn_then_PassesIt() {
        tableParser.addRecordFilter(mock(Predicate.class));
        tableParser.addRecordFilter(mock(Predicate.class));
        tableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertEquals("record filters added", 2, passedRecordFilters.size());
    }

    @Test
    public void when_MultiLineText_then_ParsesEachRow() {
        tableParser.parse();
        getPassedValuesAfterParse();
        Assert.assertArrayEquals(LINES, passedLines.toArray(new String[0]));
    }

    @Test
    public void when_FinishesParsing_then_TryWithResourcesClosesReader() throws IOException {
        Exception testEx = new RuntimeException("test exception");
        Mockito.doThrow(testEx).when(mockedParserLogic).parseRawLine(eq(LINE2));
        try (StringTableParser parser = tableParser) {
            parser.parse();
            Assert.fail("Exception should be thrown");
        } catch (Exception e) {
            Assert.assertSame("Expected exception thrown", testEx, e);
            Assert.assertEquals("Number of suppressed exceptions", 0, e.getSuppressed().length);
        }
        verify(spyReader).close();
    }

    @Test
    public void when_ExceptionDuringParsingAndClosing_then_SuppressedExceptionIsAvailable() throws IOException {
        RuntimeException testEx = new RuntimeException("test exception");
        Mockito.doThrow(testEx).when(mockedParserLogic).parseRawLine(eq(LINE2));

        RuntimeException closeEx = new RuntimeException("closing exception");
        Mockito.doThrow(closeEx).when(spyReader).close();

        try (StringTableParser parser = tableParser) {
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
        verify(spyTableParserLogicFactory).createNew(eq(spyRecParser), headerCaptor.capture(), lineFilterCaptor.capture(), recordFilterCaptor.capture());
        passedFirstRowIsHeader = headerCaptor.getValue();
        passedLineFilters = lineFilterCaptor.getValue();
        passedRecordFilters = recordFilterCaptor.getValue();

        ArgumentCaptor<String> lineCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockedParserLogic, times(LINES.length)).parseRawLine(lineCaptor.capture());
        passedLines = lineCaptor.getAllValues();
    }


}
