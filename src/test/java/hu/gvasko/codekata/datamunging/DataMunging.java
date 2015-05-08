package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringtable.*;
import org.junit.Assert;
import org.junit.Test;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;

import java.net.URL;

/**
 *
 * Created by gvasko on 2015.05.06..
 */
public class DataMunging {

    // Weather Columns
    static final int WEATHER_DAY_LEN = 5;
    static final int WEATHER_MAX_T_LEN = 6;
    static final int WEATHER_MIN_T_LEN = 6;
    static final String WEATHER_DAY_NAME = "Dy";
    static final String WEATHER_MAX_T_NAME = "MxT";
    static final String WEATHER_MIN_T_NAME = "MnT";

    // Football Columns
    static final int FOOTBALL_NUM_LEN = 7;
    static final int FOOTBALL_TEAM_LEN = 16;
    static final int FOOTBALL_P_LEN = 6;
    static final int FOOTBALL_W_LEN = 4;
    static final int FOOTBALL_L_LEN = 4;
    static final int FOOTBALL_D_LEN = 6;
    static final int FOOTBALL_F_LEN = 4;
    static final int FOOTBALL_MINUS_LEN = 3;
    static final int FOOTBALL_A_LEN = 6;
    static final int FOOTBALL_PTS_LEN = 3;
    static final String FOOTBALL_TEAM_NAME = "Team";
    static final String FOOTBALL_GOALS_FOR_NAME = "F";
    static final String FOOTBALL_GOALS_AGAINST_NAME = "A";

    @Test
    public void testDayWithSmallestTemperatureSpread() throws Exception {
        URL datFile = this.getClass().getResource("weather.dat");
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(datFile.toURI())) {
            StringTable table = parser.firstRowIsHeader().excludeLastRow().excludeEmptyRows().parse(
                    WEATHER_DAY_LEN, WEATHER_MAX_T_LEN, WEATHER_MIN_T_LEN
            );

            table.addStringDecoder(new KeepIntegerOnly(), WEATHER_MAX_T_NAME, WEATHER_MIN_T_NAME);

            String dayOfSmallestTemperatureSpread = "14";
            StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(table.getAllRecords(), WEATHER_MAX_T_NAME, WEATHER_MIN_T_NAME);
            String actualDay = actualRecord.get(WEATHER_DAY_NAME);
            Assert.assertEquals(dayOfSmallestTemperatureSpread, actualDay);
        }
    }

    @Test
    public void testNameOfTeamWithSmallestGoalDifference() throws Exception {
        URL datFile = this.getClass().getResource("football.dat");
        try (StringTableParser parser = StringTableFactory.getInstance().getParser(datFile.toURI())) {
            StringTable table = parser.firstRowIsHeader().excludeEmptyRows().parse(
                    FOOTBALL_NUM_LEN, FOOTBALL_TEAM_LEN, FOOTBALL_P_LEN, FOOTBALL_W_LEN,
                    FOOTBALL_L_LEN, FOOTBALL_D_LEN, FOOTBALL_F_LEN, FOOTBALL_MINUS_LEN,
                    FOOTBALL_A_LEN, FOOTBALL_PTS_LEN
            );

            String nameOfTeamWithSmallestGoalDifference = "Aston_Villa";
            StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(table.getAllRecords(), FOOTBALL_GOALS_FOR_NAME, FOOTBALL_GOALS_AGAINST_NAME);
            String actualTeamName = actualRecord.get(FOOTBALL_TEAM_NAME);
            Assert.assertEquals(nameOfTeamWithSmallestGoalDifference, actualTeamName);
        }
    }
}
