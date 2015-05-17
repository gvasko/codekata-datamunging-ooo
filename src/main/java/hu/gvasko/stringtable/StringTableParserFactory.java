package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

/**
 * Created by gvasko on 2015.05.17..
 */
interface StringTableParserFactory {
    StringTableParser createNew(StringRecordParser recordParser, URI uri) throws IOException;
    StringTableParser createNew(StringRecordParser recordParser, Reader reader);

}
