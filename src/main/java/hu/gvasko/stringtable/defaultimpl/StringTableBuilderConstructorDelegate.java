package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringtable.StringTableBuilder;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringTableBuilderConstructorDelegate {
    StringTableBuilder call(String... schema);
    StringRecordBuilder call();
}
