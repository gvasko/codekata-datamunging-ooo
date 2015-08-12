package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.di.ConversionConstructorDelegate;
import hu.gvasko.stringrecord.StringRecord;

import java.util.Map;

/**
 * Created by gvasko on 2015.05.22..
 */
interface StringRecordConstructorDelegate extends ConversionConstructorDelegate<Map<String, String>, StringRecord> {
}
