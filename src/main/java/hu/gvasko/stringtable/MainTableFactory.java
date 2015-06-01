package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

/**
 * Created by gvasko on 2015.06.01..
 */
public interface MainTableFactory {
    StringTableBuilder newStringTableBuilder(String... schema);

    StringTableParser newStringTableParser(StringRecordParser recordParser, URI uri) throws IOException;

    StringTableParser newStringTableParser(StringRecordParser recordParser, Reader reader);
}
