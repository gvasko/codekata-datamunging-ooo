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
import static org.hamcrest.Matchers.*;

/**
 * Created by gvasko on 2015.09.30..
 */
@Category(SystemLevelTest.class)
public class DataMungingConsoleAppErrorTest {

    @Test
    public void given_weatherDatFile_when_fileNotFound_then_errorMessage() throws Exception {
        String file = "not.found";
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
                "--" + LINE_PARSER, LINE_PARSER_FIX_WIDTH,
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("No exception",
                consoleAppOutput, not(containsString("exception")));

        assertThat("Error message in the output",
                consoleAppOutput, containsString("File not found"));
    }

    @Test
    public void given_weatherDatFile_when_missingArgs_then_errorMessage() throws Exception {
        String file = Paths.get(WeatherFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_TEMP.columnName() + "," + WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + DIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
//                "--" + DIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(WeatherFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                "--" + CHARSET, "US-ASCII",
                "--" + LINE_PARSER, LINE_PARSER_FIX_WIDTH,
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("No exception",
                consoleAppOutput, not(containsString("exception")));

        assertThat("Error message in the output",
                consoleAppOutput, containsString("Argument not passed"));
    }

    @Test
    public void given_weatherCsvFile_when_malformedCSV_then_errorMessage() throws Exception {
        String file = Paths.get(DataMungingConsoleAppErrorTest.class.getResource("weather.malformed.csv").toURI()).toString();
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
                "--" + LINE_PARSER, LINE_PARSER_CSV,
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("No exception",
                consoleAppOutput, not(containsString("exception")));

        assertThat("Error message in the output",
                consoleAppOutput, containsString("Minimal number of columns"));
    }

    @Test
    public void given_footballDatFile_when_spaceSepParser_then_cannotParse() throws Exception {
        String file = Paths.get(FootballFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + SKIP_SPLITTER_LINES,
                "--" + DIFF_1_COLUMN, FootballFixture.GOALS_FOR.columnName(),
                "--" + DIFF_2_COLUMN, FootballFixture.GOALS_AGAINST.columnName(),
                "--" + DIFF_RETURN_COLUMN, FootballFixture.TEAM.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(FootballFixture.columnCount()),
                "--" + CHARSET, "US-ASCII",
                "--" + LINE_PARSER, LINE_PARSER_SPACE_SEP,
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The name of the team with the smallest difference in goals",
                consoleAppOutput, is(not(equalTo("Aston_Villa" + System.lineSeparator()))));
    }

    @Test
    public void given_weatherDatFile_when_spaceSepParser_then_dayWithSmallestRSpreadIs14() throws Exception {
        String file = Paths.get(WeatherFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_R.columnName() + "," + WeatherFixture.MIN_R.columnName(),
                "--" + DIFF_1_COLUMN, WeatherFixture.MIN_R.columnName(),
                "--" + DIFF_2_COLUMN, WeatherFixture.MAX_R.columnName(),
                "--" + DIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(WeatherFixture.columnCount()),
                "--" + CHARSET, "US-ASCII",
                "--" + LINE_PARSER, LINE_PARSER_SPACE_SEP,
                "--" + FUNCTIONS, FUNCTION_NAME_MINDIFF,
                file
        };

        String consoleAppOutput = runApp(arguments);

        assertThat("The day of the smallest temperature spread",
                consoleAppOutput, is(not(equalTo("14" + System.lineSeparator()))));
    }



}
