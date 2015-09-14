package hu.gvasko.codekata.datamunging;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by gvasko on 2015.05.22..
 */
public enum FootballFixture {
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

    public static int columnCount() {
        return values().length;
    }

    public static int[] widthsAsArray() {
        return Arrays.stream(values()).mapToInt(FootballFixture::columnWidth).toArray();
    }

    public static URI getDatFile() {
        try {
            return FootballFixture.class.getResource("football.dat").toURI();
        }
        catch (URISyntaxException ex) {
            // We trust that getResource returns a valid URL,
            // so URISyntaxException cannot be thrown here.
            throw new RuntimeException("getResource URL to URI", ex);
        }
    }

    public static URI getCSVFile() {
        try {
            return FootballFixture.class.getResource("football.csv").toURI();
        }
        catch (URISyntaxException ex) {
            // We trust that getResource returns a valid URL,
            // so URISyntaxException cannot be thrown here.
            throw new RuntimeException("getResource URL to URI", ex);
        }
    }

    public static Charset getCharset() {
        return Charset.forName("US-ASCII");
    }

}
