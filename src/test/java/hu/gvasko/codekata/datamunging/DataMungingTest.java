package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.*;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.CSVParserImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import hu.gvasko.testutils.categories.IntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
// TODO: unfortunately hamcrest is not available in Idea & Gradle project
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;


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
    public void given_weatherFixWidthDatFile_when_dayWithSmallestTemperatureSpread_then_returns14() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(WeatherFixture.widthsAsArray());
        try (StringTableParser parser = factory.createStringTableParser(recordParser, WeatherFixture.getDatFile())) {
            assertThatDayWithSmallestTemperatureSpreadIs14(parser);
        }
    }

    @Test
    public void given_weatherCsvFile_when_dayWithSmallestTemperatureSpread_then_returns14() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(WeatherFixture.columnCount());
        try (StringTableParser parser = factory.createStringTableParser(recordParser, WeatherFixture.getCSVFile())) {
            assertThatDayWithSmallestTemperatureSpreadIs14(parser);
        }
    }

    private void assertThatDayWithSmallestTemperatureSpreadIs14(StringTableParser parser) {
        parser.addLineFilter(factory.getCommonLineFilters().skipEmptyLines());
        parser.addRecordFilter(factory.getCommonRecordFilters().onlyNumbersInColumn(WeatherFixture.DAY.columnName()));
        StringTable table = parser.firstRowIsHeader().parse();

        table.addStringDecoderToColumns(
                factory.getCommonDecoders().keepIntegersOnly(),
                WeatherFixture.MAX_TEMP.columnName(),
                WeatherFixture.MIN_TEMP.columnName());

        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                WeatherFixture.MAX_TEMP.columnName(),
                WeatherFixture.MIN_TEMP.columnName());

        String dayOfSmallestTemperatureSpread = "14";
        String actualDay = actualRecord.get(WeatherFixture.DAY.columnName());
        Assert.assertEquals(dayOfSmallestTemperatureSpread, actualDay);
    }

    @Test
    public void given_footballFixWidthDatFile_when_nameOfTeamWithSmallestGoalDifference_then_returnsAstonVilla() throws Exception {
        StringRecordParser recordParser = new FixWidthTextParserImpl(FootballFixture.widthsAsArray());
        try (StringTableParser parser = factory.createStringTableParser(recordParser, FootballFixture.getDatFile())) {
            assertThatNameOfTeamWithSmallestGoalDifferenceIsAstonVilla(parser);
        }
    }

    @Test
    public void given_footballCsvFile_when_nameOfTeamWithSmallestGoalDifference_then_returnsAstonVilla() throws Exception {
        StringRecordParser recordParser = new CSVParserImpl(FootballFixture.columnCount());
        try (StringTableParser parser = factory.createStringTableParser(recordParser, FootballFixture.getCSVFile())) {
            assertThatNameOfTeamWithSmallestGoalDifferenceIsAstonVilla(parser);
        }
    }

    private void assertThatNameOfTeamWithSmallestGoalDifferenceIsAstonVilla(StringTableParser parser) {
        parser.addLineFilter(factory.getCommonLineFilters().skipEmptyLines());
        parser.addLineFilter(factory.getCommonLineFilters().skipSplitterLines());
        StringTable table = parser.firstRowIsHeader().parse();

        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                FootballFixture.GOALS_FOR.columnName(),
                FootballFixture.GOALS_AGAINST.columnName());

        String nameOfTeamWithSmallestGoalDifference = "Aston_Villa";
        String actualTeamName = actualRecord.get(FootballFixture.TEAM.columnName());
        Assert.assertEquals(nameOfTeamWithSmallestGoalDifference, actualTeamName);
    }

}
