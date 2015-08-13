package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.di.ConversionConstructorDelegate;
import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;
import hu.gvasko.stringtable.StringTableBuilder;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringTableBuilderConstructorDelegate extends ConversionConstructorDelegate<String[], StringTableBuilder> {
    StringRecordBuilderConstructorDelegate getRecordBuilderConstructor();
}
