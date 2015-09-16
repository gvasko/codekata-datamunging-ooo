package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.testutils.categories.ClassLevelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;


/**
 * Created by gvasko on 2015.05.24..
 */
@Category(ClassLevelTest.class)
public class StringTableParserContextTest {

    private static final String LINE1 = "line1";
    private static final String LINE2 = "line2";
    private static final String[] LINES = { LINE1, LINE2 };
    private static final String TEXT = String.join("\n", LINES);

    private StringTableParser sutTableParser;
    private TableParserLogic spyParserLogic;
    private StringTableFactoryExt spyTableFactory;
    private StringRecordParser dummyRecParser;
    private Reader fakeReader;

    private Boolean passedFirstRowIsHeader;
    private List<Predicate<String>> passedLineFilters;
    private List<Predicate<StringRecord>> passedRecordFilters;
    private List<String> passedLines;

    @Before
    @SuppressWarnings("unchecked")
    public void given() {
        spyTableFactory = mock(StringTableFactoryExt.class);
        spyParserLogic = mock(TableParserLogic.class);
        when(spyParserLogic.getTable()).thenReturn(mock(StringTable.class));
        when(spyTableFactory.createTableParserLogic(any(), anyBoolean(), anyList(), anyList())).thenReturn(spyParserLogic);
        dummyRecParser = mock(StringRecordParser.class);
        fakeReader = spy(new StringReader(TEXT));

        sutTableParser = new DefaultTableParserLineReaderImpl(spyTableFactory, dummyRecParser, fakeReader);

        passedFirstRowIsHeader = null;
    }

    @Test
    public void when_FirstRowIsHeader_then_PassesTrue() {
        sutTableParser.firstRowIsHeader().parse();
        getPassedValuesAfterParse();
        assertThat("First row is header", passedFirstRowIsHeader, is(true));
    }

    @Test
    public void when_FirstRowIsNotHeader_then_PassesFalse() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        assertThat("First row is not header", passedFirstRowIsHeader, is(false));
    }

    @Test
    public void when_NoFilterAddedToColumn_then_NothingPassed() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        assertThat("No line filters added", passedLineFilters, is(empty()));
        assertThat("No record filters added", passedRecordFilters, is(empty()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddLineFilterToColumn_then_PassesIt() {
        sutTableParser.addLineFilter(mock(Predicate.class));
        sutTableParser.addLineFilter(mock(Predicate.class));
        sutTableParser.parse();
        getPassedValuesAfterParse();
        assertThat("Line filters added", passedLineFilters, hasSize(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_AddRecordFilterToColumn_then_PassesIt() {
        sutTableParser.addRecordFilter(mock(Predicate.class));
        sutTableParser.addRecordFilter(mock(Predicate.class));
        sutTableParser.parse();
        getPassedValuesAfterParse();
        assertThat("Record filters added", passedRecordFilters, hasSize(2));
    }

    @Test
    public void when_MultiLineText_then_ParsesEachRow() {
        sutTableParser.parse();
        getPassedValuesAfterParse();
        String[] passedLinesAsArray = passedLines.toArray(new String[0]);
        assertThat(passedLinesAsArray, is(equalTo(LINES)));
    }

    @Test
    public void when_ParsingFinished_then_TryWithResourcesClosesReader() throws IOException {
        Exception testEx = new RuntimeException("test exception");
        Mockito.doThrow(testEx).when(spyParserLogic).parseRawLine(eq(LINE2));
        try (StringTableParser parser = sutTableParser) {
            parser.parse();
            Assert.fail("Exception should have been thrown");
        } catch (Exception e) {
            assertThat("Expected exception thrown", e, is(sameInstance(testEx)));
            assertThat("Number of suppressed exceptions", Arrays.asList(e.getSuppressed()), is(empty()));
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
            assertThat("Expected exception thrown", e, is(sameInstance(testEx)));
            assertThat("Number of suppressed exceptions", Arrays.asList(e.getSuppressed()), hasSize(1));
            assertThat("Expected suppressed exception", e.getSuppressed()[0], is(sameInstance(closeEx)));
        }
    }

    @SuppressWarnings("unchecked")
    private void getPassedValuesAfterParse() {
        ArgumentCaptor<Boolean> headerCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> lineFilterCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> recordFilterCaptor = ArgumentCaptor.forClass(List.class);
        verify(spyTableFactory).createTableParserLogic(eq(dummyRecParser), headerCaptor.capture(), lineFilterCaptor.capture(), recordFilterCaptor.capture());
        passedFirstRowIsHeader = headerCaptor.getValue();
        passedLineFilters = lineFilterCaptor.getValue();
        passedRecordFilters = recordFilterCaptor.getValue();

        ArgumentCaptor<String> lineCaptor = ArgumentCaptor.forClass(String.class);
        verify(spyParserLogic, times(LINES.length)).parseRawLine(lineCaptor.capture());
        passedLines = lineCaptor.getAllValues();
    }


}
