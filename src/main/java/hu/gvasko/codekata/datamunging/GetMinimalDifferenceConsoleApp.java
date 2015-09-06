package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringrecord.defaultimpl.DefaultMainRecordFactoryImpl;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.stringtable.defaultimpl.DefaultMainTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.CSVParserImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by gvasko on 2015.09.05..
 */
// TODO: unit tests?
public class GetMinimalDifferenceConsoleApp {

    public static final String FIRST_LINE_IS_HEADER = "first-line-is-header";
    public static final String SKIP_EMPTY_LINES = "skip-empty-lines";
    public static final String SKIP_SPLITTER_LINES = "skip-splitter-lines";
    public static final String KEEP_RECORDS_IF_NUMERIC = "keep-records-if-numeric";
    public static final String DECODE_AS_INTEGER = "decode-as-integer";
    public static final String MINDIFF_1_COLUMN = "mindiff-1";
    public static final String MINDIFF_2_COLUMN = "mindiff-2";
    public static final String MINDIFF_RETURN_COLUMN = "mindiff-return";
    public static final String COLUMN_WIDTHS = "column-widths";
    public static final String COLUMN_COUNT = "column-count";

    private CommandLine commandLine;
    private StringTable table;

    public static void main(String[] args) throws Exception {
        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine commandLine = commandLineParser.parse(createOptions(), args);
            validateCommandLine(commandLine);

            GetMinimalDifferenceConsoleApp app = new GetMinimalDifferenceConsoleApp(commandLine);
            app.run();
        }
        catch (ParseException pe) {
            System.err.println("Invalid arguments. " + pe.getMessage());
        }
    }

    private void run() throws Exception {
        loadTable();

        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(),
                commandLine.getOptionValue(MINDIFF_1_COLUMN),
                commandLine.getOptionValue(MINDIFF_2_COLUMN));

        String actualValue = actualRecord.get(commandLine.getOptionValue(MINDIFF_RETURN_COLUMN));
        System.out.println(actualValue);
    }

    private GetMinimalDifferenceConsoleApp(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    private static void validateCommandLine(CommandLine commandLine) throws ParseException {
        if (commandLine.getArgs().length == 0) {
            throw new ParseException("File name not provided.");
        }
        if (commandLine.getArgs().length > 1) {
            throw new ParseException("Too many arguments provided.");
        }
        String file = commandLine.getArgs()[0];
        if (Files.notExists(Paths.get(file))) {
            throw new ParseException("File not found: " + file);
        }
        if (file.toLowerCase().endsWith(".dat") && !commandLine.hasOption(COLUMN_WIDTHS)) {
            throw new ParseException(".dat files consist of fix-width columns, please specify " + COLUMN_WIDTHS);
        }
        if (file.toLowerCase().endsWith(".csv") && !commandLine.hasOption(COLUMN_COUNT)) {
            throw new ParseException(".csv files needs expected number of columns for validation, please specify " + COLUMN_WIDTHS);
        }
    }

    private void loadTable() throws Exception {
        // TODO: this is tight
        DefaultMainTableFactoryImpl factory = new DefaultMainTableFactoryImpl(DefaultMainRecordFactoryImpl.createGuiceModule());
        String filePath = commandLine.getArgs()[0];

        StringRecordParser recParser = getRecParser(filePath);

        try (StringTableParser parser = factory.newStringTableParser(recParser, Paths.get(filePath).toUri())) {
            if (commandLine.hasOption(FIRST_LINE_IS_HEADER)) {
                parser.firstRowIsHeader();
            }
            if (commandLine.hasOption(SKIP_EMPTY_LINES)) {
                parser.addLineFilter(factory.skipEmptyLines());
            }
            if (commandLine.hasOption(SKIP_SPLITTER_LINES)) {
                parser.addLineFilter(factory.skipSplitterLines());
            }
            if (commandLine.hasOption(KEEP_RECORDS_IF_NUMERIC)) {
                for (String column : commandLine.getOptionValue(KEEP_RECORDS_IF_NUMERIC).split(",")) {
                    parser.addRecordFilter(factory.onlyNumbersInColumn(column));
                }
            }
            table = parser.parse();
        }

        if (commandLine.hasOption(DECODE_AS_INTEGER)) {
            table.addStringDecoderToColumns(
                    factory.keepIntegersOnly(),
                    commandLine.getOptionValue(DECODE_AS_INTEGER).split(","));
        }
    }

    private StringRecordParser getRecParser(String filePath) {
        if (filePath.endsWith(".dat")) {
            String[] columnWidthsStr = commandLine.getOptionValue(COLUMN_WIDTHS).split(",");
            return new FixWidthTextParserImpl(Stream.of(columnWidthsStr).mapToInt(Integer::parseInt).toArray());
        }
        if (filePath.endsWith(".csv")) {
            int columnCount = Integer.parseInt(commandLine.getOptionValue(COLUMN_COUNT));
            return new CSVParserImpl(columnCount);
        }
        throw new RuntimeException("Unknown file extension.");
    }

    private static Options createOptions() {
        Options options = new Options();

        options.addOption(Option.builder().longOpt(FIRST_LINE_IS_HEADER).desc("process the first line as header").build());
        options.addOption(Option.builder().longOpt(SKIP_EMPTY_LINES).desc("do not process empty lines of the input file").build());
        options.addOption(Option.builder().longOpt(SKIP_SPLITTER_LINES).desc("do not process lines consisting of the same character").build());
        options.addOption(Option.builder().longOpt(KEEP_RECORDS_IF_NUMERIC).hasArg().argName("COLUMN-NAMES").desc("keep the record if the values in the given columns are numeric, filter otherwise").build());
        options.addOption(Option.builder().longOpt(DECODE_AS_INTEGER).hasArg().argName("COLUMN-NAMES").desc("treat the given columns as integer and ignore other characters").build());
        options.addOption(Option.builder().longOpt(MINDIFF_1_COLUMN).required().hasArg().argName("COLUMN-NAME").desc("one column name to to calculate difference").build());
        options.addOption(Option.builder().longOpt(MINDIFF_2_COLUMN).required().hasArg().argName("COLUMN-NAME").desc("another column name to calculate difference").build());
        options.addOption(Option.builder().longOpt(MINDIFF_RETURN_COLUMN).required().hasArg().argName("COLUMN-NAME").desc("the program returns the value belonging to this column in the record where the difference is minimal").build());
        options.addOption(Option.builder().longOpt(COLUMN_WIDTHS).hasArg().argName("COLUMN-WIDTHS").desc("fix widths of the columns in the given file").build());
        options.addOption(Option.builder().longOpt(COLUMN_COUNT).hasArg().argName("NUMBER").desc("expected number of columns").build());

        return options;
    }
}
