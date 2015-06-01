package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringtable.StringRecordParser;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringRecordParserFactory {
    StringRecordParser createNew();
}
