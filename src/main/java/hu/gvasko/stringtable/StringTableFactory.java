package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Created by gvasko on 2015.06.01..
 */
public interface StringTableFactory {
    StringTableBuilder createStringTableBuilder(String... schema );
    StringTableParser createStringTableParser(StringRecordParser recordParser, URI uri, Charset charset)  throws IOException;
    StringTableParser createStringTableParser(StringRecordParser recordParser, Reader reader);
}
