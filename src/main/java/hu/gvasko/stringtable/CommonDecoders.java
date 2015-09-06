package hu.gvasko.stringtable;

import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.09.06..
 */
public interface CommonDecoders {
    UnaryOperator<String> keepIntegersOnly();
}
