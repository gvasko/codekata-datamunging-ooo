package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordFactory;

import java.util.Map;

/**
 * Created by gvasko on 2015.09.06..
 */
interface StringRecordFactoryExt extends StringRecordFactory {
    StringRecord createStringRecord(Map<String, String> sharedFields);
}
