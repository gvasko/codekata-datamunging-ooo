package hu.gvasko.codekata.datamunging;

import hu.gvasko.testutils.categories.SystemLevelTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.nio.file.Paths;
import java.util.Arrays;

import static hu.gvasko.codekata.datamunging.DataMungingConsoleApp.*;
import static hu.gvasko.codekata.datamunging.FunctionMinDiff.*;
import static hu.gvasko.codekata.datamunging.DataMungingConsoleAppTestUtil.runApp;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by gvasko on 2015.09.05..
 */
@Category(SystemLevelTest.class)
public class DataMungingConsoleAppTest {

    @Test
    public void given_weatherDatFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        String file = Paths.get(WeatherFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_TEMP.columnName() + "," + WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
                "--" + DIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(WeatherFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                "--" + CHARSET, "US-ASCII",
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The day of the smallest temperature spread",
                consoleAppOutput, is(equalTo("14" + System.lineSeparator())));
    }

    @Test
    public void given_weatherCsvFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        String file = Paths.get(WeatherFixture.getCSVFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_TEMP.columnName() + "," + WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
                "--" + DIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(WeatherFixture.columnCount()),
                "--" + CHARSET, "US-ASCII",
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The day of the smallest temperature spread",
                consoleAppOutput, is(equalTo("14" + System.lineSeparator())));
    }

    @Test
    public void given_footballDatFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        String file = Paths.get(FootballFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + SKIP_SPLITTER_LINES,
                "--" + DIFF_1_COLUMN, FootballFixture.GOALS_FOR.columnName(),
                "--" + DIFF_2_COLUMN, FootballFixture.GOALS_AGAINST.columnName(),
                "--" + DIFF_RETURN_COLUMN, FootballFixture.TEAM.columnName(),
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(FootballFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                "--" + CHARSET, "US-ASCII",
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The name of the team with the smallest difference in goals",
                consoleAppOutput, is(equalTo("Aston_Villa" + System.lineSeparator())));
    }

    @Test
    public void given_footballCsvFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        String file = Paths.get(FootballFixture.getCSVFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + SKIP_SPLITTER_LINES,
                "--" + DIFF_1_COLUMN, FootballFixture.GOALS_FOR.columnName(),
                "--" + DIFF_2_COLUMN, FootballFixture.GOALS_AGAINST.columnName(),
                "--" + DIFF_RETURN_COLUMN, FootballFixture.TEAM.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(FootballFixture.columnCount()),
                "--" + CHARSET, "US-ASCII",
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The name of the team with the smallest difference in goals",
                consoleAppOutput, is(equalTo("Aston_Villa" + System.lineSeparator())));
    }


}
