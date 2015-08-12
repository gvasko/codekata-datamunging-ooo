package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTableParser;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringTableParserConstructorDelegate {
    StringTableParser call(StringRecordParser recordParser, URI uri) throws IOException;
    StringTableParser call(StringRecordParser recordParser, Reader reader);

}
