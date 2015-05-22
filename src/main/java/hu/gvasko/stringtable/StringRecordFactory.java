package hu.gvasko.stringtable;

import java.util.Map;

/**
 * Created by gvasko on 2015.05.22..
 */
public interface StringRecordFactory {
    StringRecord createNew(Map<String, String> stringMap);
}
