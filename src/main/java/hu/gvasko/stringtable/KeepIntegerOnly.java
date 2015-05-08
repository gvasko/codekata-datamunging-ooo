package hu.gvasko.stringtable;

import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.06..
 */
class KeepIntegerOnly implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
