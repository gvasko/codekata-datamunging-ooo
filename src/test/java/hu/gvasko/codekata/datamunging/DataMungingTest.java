package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.*;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.CSVParserImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import hu.gvasko.testutils.categories.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * Created by gvasko on 2015.05.06..
 */
@Category(IntegrationTest.class)
public class DataMungingTest {

    private StringTableFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultStringTableFactoryImpl(new DefaultStringRecordFactoryImpl());
    }

    @Test
    public void given_weatherDatFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(WeatherFixture.widthsAsArray());
        testDayWithSmallestTemperatureSpreadIs14(recordParser, WeatherFixture.getDatFile());
    }

    @Test
    public void given_weatherCsvFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(WeatherFixture.columnCount());
        testDayWithSmallestTemperatureSpreadIs14(recordParser, WeatherFixture.getCSVFile());
    }

    @Test
    public void given_footballDatFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(FootballFixture.widthsAsArray());
        testSmallestDifferenceInGoalsIsAstonVilla(recordParser, FootballFixture.getDatFile());
    }

    @Test
    public void given_footballCsvFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(FootballFixture.columnCount());
        testSmallestDifferenceInGoalsIsAstonVilla(recordParser, FootballFixture.getCSVFile());
    }

    private void testDayWithSmallestTemperatureSpreadIs14(StringRecordParser recordParser, URI file) throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(recordParser, file)) {
            StringTable table = parseWeatherTable(parser);
            String resultDay = getDayOfSmallestTemperatureSpread(table);
            String expectedDay = "14";
            assertThat("The day of the smallest temperature spread", resultDay, is(equalTo(expectedDay)));
        }
    }

    private void testSmallestDifferenceInGoalsIsAstonVilla(StringRecordParser recordParser, URI file) throws Exception {
        try (StringTableParser parser = factory.createStringTableParser(recordParser, file)) {
            StringTable table = parseFootballTable(parser);
            String resultTeamName = getTeamNameWithSmallestGoalDifference(table);
            String expectedTeamName = "Aston_Villa";
            assertThat("The name of the team with the smallest difference in goals",
                    resultTeamName, is(equalTo(expectedTeamName)));
        }
    }

    private StringTable parseWeatherTable(StringTableParser parser) {
        parser.addLineFilter(factory.getCommonLineFilters().skipEmptyLines());
        parser.addRecordFilter(factory.getCommonRecordFilters().onlyNumbersInColumn(WeatherFixture.DAY.columnName()));
        StringTable table = parser.firstRowIsHeader().parse();

        table.addStringDecoderToColumns(
                factory.getCommonDecoders().keepIntegersOnly(),
                WeatherFixture.MAX_TEMP.columnName(),
                WeatherFixture.MIN_TEMP.columnName());

        return table;
    }

    private StringTable parseFootballTable(StringTableParser parser) {
        parser.addLineFilter(factory.getCommonLineFilters().skipEmptyLines());
        parser.addLineFilter(factory.getCommonLineFilters().skipSplitterLines());
        StringTable table = parser.firstRowIsHeader().parse();
        return table;
    }

    private String getDayOfSmallestTemperatureSpread(StringTable table) {
        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                WeatherFixture.MAX_TEMP.columnName(),
                WeatherFixture.MIN_TEMP.columnName());

        return actualRecord.get(WeatherFixture.DAY.columnName());
    }

    private String getTeamNameWithSmallestGoalDifference(StringTable table) {
        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                FootballFixture.GOALS_FOR.columnName(),
                FootballFixture.GOALS_AGAINST.columnName());

        return actualRecord.get(FootballFixture.TEAM.columnName());
    }

}
