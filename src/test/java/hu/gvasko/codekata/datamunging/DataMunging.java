package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringtable.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
// TODO: unfortunately hamcrest is not available in Idea & Gradle project
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;

import java.net.URL;
import java.util.Arrays;

/**
 *
 * Created by gvasko on 2015.05.06..
 */
public class DataMunging {

    private enum WeatherColumns {
        DAY("Dy", 5),
        MAX_TEMP("MxT", 6),
        MIN_TEMP("MnT", 6);

        private String name;
        private int len;

        WeatherColumns(String name, int len) {
            this.name = name;
            this.len = len;
        }

        public String getName() {
            return name;
        }

        public int getLen() {
            return len;
        }

        public static int[] getLenArray() {
            return Arrays.stream(values()).mapToInt(WeatherColumns::getLen).toArray();
        }
    }

    private enum FootballColumns {
        NUM("?", 7),
        TEAM("Team", 16),
        P("P", 6),
        W("W", 4),
        L("L", 4),
        D("D", 6),
        GOALS_FOR("F", 4),
        MINUS("?", 3),
        GOALS_AGAINST("A", 6),
        PTS("Points", 3);

        private String name;
        private int len;

        FootballColumns(String name, int len) {
            this.name = name;
            this.len = len;
        }

        public String getName() {
            return name;
        }

        public int getLen() {
            return len;
        }

        public static int[] getLenArray() {
            return Arrays.stream(values()).mapToInt(FootballColumns::getLen).toArray();
        }
    }

    private StringTableFactory factory;

    @Before
    public void setUp() {
        factory = StringTableFactory.getInstance();
    }

    @Test
    public void testDayWithSmallestTemperatureSpread() throws Exception {
        URL datFile = this.getClass().getResource("weather.dat");
        try (StringTableParser parser = factory.newStringTableParser(datFile.toURI())) {
            parser.addLineFilter(factory.skipEmptyLines());
            parser.addRecordFilter(factory.onlyNumbersInColumn(WeatherColumns.DAY.getName()));
            StringTable table = parser.firstRowIsHeader().parse(factory.getFixWidthRecordParser(WeatherColumns.getLenArray()));

            table.addStringDecoderToColumns(
                    factory.keepIntegersOnly(),
                    WeatherColumns.MAX_TEMP.getName(),
                    WeatherColumns.MIN_TEMP.getName());

            String dayOfSmallestTemperatureSpread = "14";
            StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                    table.getAllRecords(),
                    WeatherColumns.MAX_TEMP.getName(),
                    WeatherColumns.MIN_TEMP.getName());
            String actualDay = actualRecord.get(WeatherColumns.DAY.getName());
            Assert.assertEquals(dayOfSmallestTemperatureSpread, actualDay);
        }
    }

    @Test
    public void testNameOfTeamWithSmallestGoalDifference() throws Exception {
        URL datFile = this.getClass().getResource("football.dat");
        try (StringTableParser parser = factory.newStringTableParser(datFile.toURI())) {
            parser.addLineFilter(factory.skipEmptyLines());
            parser.addLineFilter(factory.skipSplitterLines());
            StringTable table = parser.firstRowIsHeader().parse(factory.getFixWidthRecordParser(FootballColumns.getLenArray()));

            String nameOfTeamWithSmallestGoalDifference = "Aston_Villa";
            StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                    table.getAllRecords(),
                    FootballColumns.GOALS_FOR.getName(),
                    FootballColumns.GOALS_AGAINST.getName());
            String actualTeamName = actualRecord.get(FootballColumns.TEAM.getName());
            Assert.assertEquals(nameOfTeamWithSmallestGoalDifference, actualTeamName);
        }
    }
}
