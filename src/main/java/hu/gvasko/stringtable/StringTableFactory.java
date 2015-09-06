package hu.gvasko.stringtable;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

/**
 * Created by gvasko on 2015.06.01..
 */
public interface StringTableFactory {
    CommonDecoders getCommonDecoders();
    CommonLineFilters getCommonLineFilters();
    CommonRecordFilters getCommonRecordFilters();
    StringTableBuilder createStringTableBuilder(String... schema );
    StringTableParser createStringTableParser(StringRecordParser recordParser, URI uri)  throws IOException;
    StringTableParser createStringTableParser(StringRecordParser recordParser, Reader reader);
}
