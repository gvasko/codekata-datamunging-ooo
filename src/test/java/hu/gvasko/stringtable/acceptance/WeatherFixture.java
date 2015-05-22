package hu.gvasko.stringtable.acceptance;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by gvasko on 2015.05.22..
 */
enum WeatherFixture {
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

    public static int[] widthsAsArray() {
        return Arrays.stream(values()).mapToInt(WeatherFixture::columnWidth).toArray();
    }

    public static URI getDatFile() throws Exception {
        return WeatherFixture.class.getResource("weather.dat").toURI();
    }

    public static URI getCSVFile() throws Exception {
        return WeatherFixture.class.getResource("weather.csv").toURI();
    }
}
