package hu.gvasko.stringtable.acceptance;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by gvasko on 2015.05.22..
 */
enum FootballFixture {
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

    FootballFixture(String name, int len) {
        this.name = name;
        this.len = len;
    }

    public String columnName() {
        return name;
    }

    public int columnWidth() {
        return len;
    }

    public static int[] widthsAsArray() {
        return Arrays.stream(values()).mapToInt(FootballFixture::columnWidth).toArray();
    }

    public static URI getDatFile() throws Exception {
        return WeatherFixture.class.getResource("football.dat").toURI();
    }
}
