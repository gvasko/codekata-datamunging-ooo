package hu.gvasko.stringtable.acceptance;

import hu.gvasko.stringtable.*;
import hu.gvasko.testutils.categories.AcceptanceTest;
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
@Category(AcceptanceTest.class)
public class DataMungingTest {

    private DefaultFactory factory;

    @Before
    public void setUp() {
        factory = DefaultFactory.getInstance();
    }

    @Test
    public void testDayWithSmallestTemperatureSpread_fixWidthText() throws Exception {
        StringRecordParser recordParser = factory.getFixWidthRecordParser(WeatherFixture.widthsAsArray());
        try (StringTableParser parser = factory.newStringTableParser(recordParser, WeatherFixture.getDatFile())) {
            assertThatDayWithSmallestTemperatureSpreadIsOK(parser);
        }
    }

    @Test
    public void testDayWithSmallestTemperatureSpread_CSV() throws Exception {
        StringRecordParser recordParser = factory.getCSVRecordParser(WeatherFixture.columnCount());
        try (StringTableParser parser = factory.newStringTableParser(recordParser, WeatherFixture.getCSVFile())) {
            assertThatDayWithSmallestTemperatureSpreadIsOK(parser);
        }
    }

    private void assertThatDayWithSmallestTemperatureSpreadIsOK(StringTableParser parser) {
        parser.addLineFilter(factory.skipEmptyLines());
        parser.addRecordFilter(factory.onlyNumbersInColumn(WeatherFixture.DAY.columnName()));
        StringTable table = parser.firstRowIsHeader().parse();

        table.addStringDecoderToColumns(
                factory.keepIntegersOnly(),
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
    public void testNameOfTeamWithSmallestGoalDifference_fixWidthText() throws Exception {
        StringRecordParser recordParser = factory.getFixWidthRecordParser(FootballFixture.widthsAsArray());
        try (StringTableParser parser = factory.newStringTableParser(recordParser, FootballFixture.getDatFile())) {
            assertThatNameOfTeamWithSmallestGoalDifferenceIsOK(parser);
        }
    }

    @Test
    public void testNameOfTeamWithSmallestGoalDifference_CSV() throws Exception {
        StringRecordParser recordParser = factory.getCSVRecordParser(FootballFixture.columnCount());
        try (StringTableParser parser = factory.newStringTableParser(recordParser, FootballFixture.getCSVFile())) {
            assertThatNameOfTeamWithSmallestGoalDifferenceIsOK(parser);
        }
    }

    private void assertThatNameOfTeamWithSmallestGoalDifferenceIsOK(StringTableParser parser) {
        parser.addLineFilter(factory.skipEmptyLines());
        parser.addLineFilter(factory.skipSplitterLines());
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
