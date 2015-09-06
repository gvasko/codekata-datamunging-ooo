package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordFactory;

import java.util.Map;

/**
 * Created by gvasko on 2015.06.01..
 */
public class DefaultStringRecordFactoryImpl implements StringRecordFactory, StringRecordFactoryExt {

    public DefaultStringRecordFactoryImpl() {
    }

    @Override
    public StringRecord createStringRecord(Map<String, String> sharedFields) {
        return new DefaultStringRecordImpl(sharedFields);
    }

    @Override
    public StringRecordBuilder createStringRecordBuilder() {
        return new DefaultStringRecordBuilderImpl(this);
    }
}
