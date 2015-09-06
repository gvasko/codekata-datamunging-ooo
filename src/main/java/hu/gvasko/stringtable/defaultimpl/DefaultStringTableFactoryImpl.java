package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.stringtable.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.06..
 */
public class DefaultStringTableFactoryImpl implements StringTableFactoryExt, CommonDecoders, CommonLineFilters, CommonRecordFilters {

    private StringRecordFactory recordFactory;

    public DefaultStringTableFactoryImpl(StringRecordFactory recordFactory) {
        this.recordFactory = recordFactory;
    }

    // TODO: move to somewhere else?

    @Override
    public CommonDecoders getCommonDecoders() {
        return this;
    }

    @Override
    public CommonLineFilters getCommonLineFilters() {
        return this;
    }

    @Override
    public CommonRecordFilters getCommonRecordFilters() {
        return this;
    }

    @Override
    public UnaryOperator<String> keepIntegersOnly() {
        return s -> {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            return sb.toString();
        };
    }

    @Override
    public Predicate<String> skipEmptyLines() {
        return line -> !"".equals(line);
    }

    @Override
    public Predicate<String> skipSplitterLines() {
        return line -> "".equals(line) || !"".equals(line.replace(line.charAt(0), ' ').trim());
    }

    @Override
    public Predicate<StringRecord> onlyNumbersInColumn(String column) {
        return record -> record.get(column).matches("^[+-]?\\d+$");
    }

    @Override
    public StringRecordFactory getRecordFactory() {
        return recordFactory;
    }

    @Override
    public StringTableBuilder createStringTableBuilder(String... schema) {
        return new DefaultStringTableBuilderImpl(this, schema);
    }

    @Override
    public StringTable createStringTable(String[] sharedSchema, List<String[]> sharedRecords) {
        return new DefaultStringTableImpl(getRecordFactory(), sharedSchema, sharedRecords);
    }

    @Override
    public StringTableParser createStringTableParser(StringRecordParser recordParser, URI uri) throws IOException {
        return new DefaultTableParserLineReaderImpl(this, recordParser, uri);
    }

    @Override
    public StringTableParser createStringTableParser(StringRecordParser recordParser, Reader reader) {
        return new DefaultTableParserLineReaderImpl(this, recordParser, reader);
    }

    @Override
    public TableParserLogic createTableParserLogic(StringRecordParser sharedRecParser) {
        return new DefaultTableParserLogicImpl(this, sharedRecParser);
    }

    @Override
    public TableParserLogic createTableParserLogic(StringRecordParser sharedRecParser, boolean isFirstRowHeader, List<Predicate<String>> sharedLineFilters, List<Predicate<StringRecord>> sharedRecordFilters) {
        return new DefaultTableParserLogicImpl(this, sharedRecParser, isFirstRowHeader, sharedLineFilters, sharedRecordFilters);
    }

    static String[] getDefaultHeader(int length) {
        String[] numberedHeader = new String[length];
        for (int i = 0; i < length; i++) {
            numberedHeader[i] = Integer.toString(i);
        }
        return numberedHeader;
    }


}
