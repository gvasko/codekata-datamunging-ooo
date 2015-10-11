package hu.gvasko.stringtable.filters;

import hu.gvasko.stringrecord.StringRecord;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.10.11..
 */
public final class StringTableFilters {

    private StringTableFilters() {
        // static class
    }

    public static UnaryOperator<String> keepIntegersOnly() {
        return s -> {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            return sb.toString();
        };
    }

    public static Predicate<String> skipEmptyLines() {
        return line -> !"".equals(line);
    }

    public static Predicate<String> skipSplitterLines() {
        return line -> "".equals(line) || !"".equals(line.replace(line.charAt(0), ' ').trim());
    }

    public static Predicate<StringRecord> onlyNumbersInColumn(String column) {
        return record -> record.get(column).matches("^[+-]?\\d+$");
    }


}
