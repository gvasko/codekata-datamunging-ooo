package hu.gvasko.stringrecord;

import hu.gvasko.stringrecord.StringRecordBuilderFactory;

/**
 * Created by gvasko on 2015.06.01..
 */
public interface MainRecordFactory {
    StringRecordBuilder newStringRecordBuilder();
    StringRecordBuilderFactory newStringRecordBuilderFactory();
}
