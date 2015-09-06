package hu.gvasko.codekata.datamunging;

import hu.gvasko.testutils.categories.AcceptanceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
                "--" + COLUMN_WIDTHS, String.join(",", String.join(",", Arrays.stream(WeatherFixture.widthsAsArray()).mapToObj(Integer::toString).toArray(String[]::new))),
                "--" + MINDIFF_1_COLUMN, WeatherFixture.MIN_TEMP.columnName(),
                "--" + MINDIFF_2_COLUMN, WeatherFixture.MAX_TEMP.columnName(),
                "--" + MINDIFF_RETURN_COLUMN, WeatherFixture.DAY.columnName(),
                file
        };

        OutputStream tempStream = new ByteArrayOutputStream();
        PrintStream testOutput = new PrintStream(tempStream, true);
        System.setOut(testOutput);

        GetMinimalDifferenceConsoleApp.main(arguments);

        Assert.assertEquals("14" + System.lineSeparator(), tempStream.toString());
    }

}
