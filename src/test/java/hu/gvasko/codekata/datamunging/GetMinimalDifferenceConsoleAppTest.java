package hu.gvasko.codekata.datamunging;

import hu.gvasko.testutils.categories.AcceptanceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static hu.gvasko.codekata.datamunging.GetMinimalDifferenceConsoleApp.*;

/**
 * Created by gvasko on 2015.09.05..
 */
@Category(AcceptanceTest.class)
public class GetMinimalDifferenceConsoleAppTest {

    @Test
    public void given_weatherDatFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        String file = Paths.get(WeatherFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_TEMP.columnName() + "," + WeatherFixture.MIN_TEMP.columnName(),
                "--" + MINDIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + MINDIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
                "--" + MINDIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(WeatherFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                file
        };

        String output = runApp(arguments);

        Assert.assertEquals("14" + System.lineSeparator(), output);
    }

    @Test
    public void given_weatherCsvFile_then_dayWithSmallestTemperatureSpreadIs14() throws Exception {
        String file = Paths.get(WeatherFixture.getCSVFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + KEEP_RECORDS_IF_NUMERIC, WeatherFixture.DAY.columnName(),
                "--" + DECODE_AS_INTEGER, WeatherFixture.MAX_TEMP.columnName() + "," + WeatherFixture.MIN_TEMP.columnName(),
                "--" + MINDIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + MINDIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
                "--" + MINDIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(WeatherFixture.columnCount()),
                file
        };

        String output = runApp(arguments);

        Assert.assertEquals("14" + System.lineSeparator(), output);
    }

    @Test
    public void given_footballDatFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        String file = Paths.get(FootballFixture.getDatFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + SKIP_SPLITTER_LINES,
                "--" + MINDIFF_1_COLUMN, FootballFixture.GOALS_FOR.columnName(),
                "--" + MINDIFF_2_COLUMN, FootballFixture.GOALS_AGAINST.columnName(),
                "--" + MINDIFF_RETURN_COLUMN, FootballFixture.TEAM.columnName(),
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(FootballFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                file
        };

        String output = runApp(arguments);

        Assert.assertEquals("Aston_Villa" + System.lineSeparator(), output);
    }

    @Test
    public void given_footballCsvFile_then_smallestDifferenceInGoalsIsAstonVilla() throws Exception {
        String file = Paths.get(FootballFixture.getCSVFile()).toString();
        String[] arguments = {
                "--" + FIRST_LINE_IS_HEADER,
                "--" + SKIP_EMPTY_LINES,
                "--" + SKIP_SPLITTER_LINES,
                "--" + MINDIFF_1_COLUMN, FootballFixture.GOALS_FOR.columnName(),
                "--" + MINDIFF_2_COLUMN, FootballFixture.GOALS_AGAINST.columnName(),
                "--" + MINDIFF_RETURN_COLUMN, FootballFixture.TEAM.columnName(),
                "--" + COLUMN_COUNT, Integer.toString(FootballFixture.columnCount()),
                file
        };

        String output = runApp(arguments);

        Assert.assertEquals("Aston_Villa" + System.lineSeparator(), output);
    }


    private String runApp(String[] arguments) throws Exception {
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        String encoding = Charset.defaultCharset().name();

        PrintStream testOutput = new PrintStream(tempStream, true, encoding);
        System.setOut(testOutput);

        GetMinimalDifferenceConsoleApp.main(arguments);
        return tempStream.toString(encoding);
    }

}
