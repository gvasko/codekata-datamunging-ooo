package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableFactory;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.CSVParserImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import hu.gvasko.stringtable.recordparsers.SpaceSeparatedTextParserImpl;
import static hu.gvasko.stringtable.filters.StringTableFilters.*;
import hu.gvasko.testutils.categories.ComponentLevelTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 *
 * Created by gvasko on 2015.05.06..
 */
@Category(ComponentLevelTest.class)
public class DataMungingTest {

    private StringTableFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultStringTableFactoryImpl(new DefaultStringRecordFactoryImpl());
    }

    @Test
    public void given_weatherDatFile_when_fixWidthParser_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(WeatherFixture.widthsAsArray());
        testDayWithSmallestTemperatureSpreadIs14(recordParser, WeatherFixture.getDatFile(), WeatherFixture.getCharset());
    }

    @Test
    public void given_weatherDatFile_when_spaceSepParser_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        // The simplest solution would be
        StringRecordParser recordParser = new SpaceSeparatedTextParserImpl(WeatherFixture.columnCount());
        testDayWithSmallestTemperatureSpreadIs14(recordParser, WeatherFixture.getDatFile(), WeatherFixture.getCharset());
    }

    @Test
    public void given_weatherCsvFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(WeatherFixture.columnCount());
        testDayWithSmallestTemperatureSpreadIs14(recordParser, WeatherFixture.getCSVFile(), WeatherFixture.getCharset());
    }

    @Test
    public void given_weatherDatFile_then_dayWithSmallestRSpreadIs14() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(WeatherFixture.widthsAsArray());
        testDayWithSmallestRSpreadIs14(recordParser, WeatherFixture.getDatFile(), WeatherFixture.getCharset());
    }

    @Test
    public void given_footballDatFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(FootballFixture.widthsAsArray());
        testSmallestDifferenceInGoalsIsAstonVilla(recordParser, FootballFixture.getDatFile(), FootballFixture.getCharset());
    }

    @Test
    public void given_footballCsvFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(FootballFixture.columnCount());
        testSmallestDifferenceInGoalsIsAstonVilla(recordParser, FootballFixture.getCSVFile(), FootballFixture.getCharset());
    }

    private void testDayWithSmallestTemperatureSpreadIs14(StringRecordParser recordParser, URI file, Charset charset) throws Exception {
        StringTable table = readWeatherTable(recordParser, file, charset);
        String resultDay = getValueAtMinDiff(table,
                WeatherFixture.MAX_TEMP.columnName(),
                WeatherFixture.MIN_TEMP.columnName(),
                WeatherFixture.DAY.columnName());
        String expectedDay = "14";
        assertThat("The day of the smallest temperature spread", resultDay, is(equalTo(expectedDay)));
    }

    private void testDayWithSmallestRSpreadIs14(StringRecordParser recordParser, URI file, Charset charset) throws Exception {
        StringTable table = readWeatherTable(recordParser, file, charset);
        String resultDay = getValueAtMinDiff(table,
                WeatherFixture.MAX_R.columnName(),
                WeatherFixture.MIN_R.columnName(),
                WeatherFixture.DAY.columnName());
        String expectedDay = "14";
        assertThat("The day of the smallest R spread", resultDay, is(equalTo(expectedDay)));
    }

    private StringTable readWeatherTable(StringRecordParser recordParser, URI file, Charset charset) throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(recordParser, file, charset)) {
            parser.addLineFilter(skipEmptyLines());
            parser.addRecordFilter(onlyNumbersInColumn(WeatherFixture.DAY.columnName()));
            StringTable table = parser.firstRowIsHeader().parse();

            table.addStringDecoderToColumns(
                    keepIntegersOnly(),
                    WeatherFixture.MAX_TEMP.columnName(),
                    WeatherFixture.MIN_TEMP.columnName());

            return table;
        }
    }

    private void testSmallestDifferenceInGoalsIsAstonVilla(StringRecordParser recordParser, URI file, Charset charset) throws Exception {
        StringTable table = readFootballTable(recordParser, file, charset);
        String resultTeamName = getTeamNameWithSmallestGoalDifference(table);
        String expectedTeamName = "Aston_Villa";
        assertThat("The name of the team with the smallest difference in goals",
                resultTeamName, is(equalTo(expectedTeamName)));
    }

    private StringTable readFootballTable(StringRecordParser recordParser, URI file, Charset charset) throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(recordParser, file, charset)) {
            parser.addLineFilter(skipEmptyLines());
            parser.addLineFilter(skipSplitterLines());
            StringTable table = parser.firstRowIsHeader().parse();
            return table;
        }
    }


    private String getValueAtMinDiff(StringTable table, String column1, String column2, String returnColumn) {
        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                column1,
                column2);

        return actualRecord.get(returnColumn);
    }

    private String getTeamNameWithSmallestGoalDifference(StringTable table) {
        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                FootballFixture.GOALS_FOR.columnName(),
                FootballFixture.GOALS_AGAINST.columnName());

        return actualRecord.get(FootballFixture.TEAM.columnName());
    }

}
