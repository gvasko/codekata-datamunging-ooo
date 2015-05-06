package hu.gvasko.codekata.datamunging;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 *
 * Created by gvasko on 2015.05.06..
 */
public class DataMunging {

    // Columns
    final int WEATHER_DAY_LEN = 5;
    final int WEATHER_MAX_T_LEN = 6;
    final int WEATHER_MIN_T_LEN = 6;
    final String WEATHER_DAY_NAME = "Dy";
    final String WEATHER_MAX_T_NAME = "MxT";
    final String WEATHER_MIN_T_NAME = "MnT";

    // load and parse weather.dat into a StringTable
    // process the table and get the day number with the smallest temperature spread
    @Test
    public void testDayOfSmallestTemperatureSpread() {
        URL datFile = this.getClass().getResource("weather.dat");
        try (StringTableParser parser = factory.getParser(datFile.toURI()) {
            // parser config: what part of the file should be parsed
            StringTable table = parser.firstRowIsHeader().excludeLastRow().excludeEmptyRows().parse(
                    WEATHER_DAY_LEN, WEATHER_MAX_T_LEN, WEATHER_MIN_T_LEN
            );

            table.addDecoder(new TrimToInteger(), WEATHER_MXT_COLUMN, WEATHER_MIN_T_NAME);

            String dayOfSmallestTemperatureSpread = "14";
            Assert.assertThat(DataMungingUtil.getFirstMinDiffRecord(table.getAllRecords(), WEATHER_MAX_T_NAME, WEATHER_MIN_T_NAME).get(WEATHER_DAY_NAME),
                    equals(dayOfSmallestTemperatureSpread));
        }
    }
}
