package hu.gvasko.codekata.datamunging;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by gvasko on 2015.05.22..
 */
public enum WeatherFixture {
    DAY("Dy", 5),
    MAX_TEMP("MxT", 6),
    MIN_TEMP("MnT", 6);

    private String name;
    private int len;

    WeatherFixture(String name, int len) {
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
        return Arrays.stream(values()).mapToInt(WeatherFixture::columnWidth).toArray();
    }

    public static URI getDatFile() {
        try {
            return WeatherFixture.class.getResource("weather.dat").toURI();
        }
        catch (URISyntaxException ex) {
            // We trust that getResource returns a valid URL,
            // so URISyntaxException cannot be thrown here.
            throw new RuntimeException("getResource URL to URI", ex);
        }
    }

    public static URI getCSVFile() throws Exception {
        try {
            return WeatherFixture.class.getResource("weather.csv").toURI();
        }
        catch (URISyntaxException ex) {
            // We trust that getResource returns a valid URL,
            // so URISyntaxException cannot be thrown here.
            throw new RuntimeException("getResource URL to URI", ex);
        }
    }
}
