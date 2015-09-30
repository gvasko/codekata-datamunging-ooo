package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.defaultimpl.DefaultStringRecordFactoryImpl;
import hu.gvasko.stringtable.StringRecordParser;
import hu.gvasko.stringtable.StringTable;
import hu.gvasko.stringtable.StringTableFactory;
import hu.gvasko.stringtable.StringTableParser;
import hu.gvasko.stringtable.defaultimpl.DefaultStringTableFactoryImpl;
import hu.gvasko.stringtable.recordparsers.CSVParserImpl;
import hu.gvasko.stringtable.recordparsers.FixWidthTextParserImpl;
import org.apache.commons.cli.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by gvasko on 2015.09.05..
 */
// TODO: unit tests?
public class DataMungingConsoleApp {

    public static final String FIRST_LINE_IS_HEADER = "first-line-is-header";
    public static final String SKIP_EMPTY_LINES = "skip-empty-lines";
    public static final String SKIP_SPLITTER_LINES = "skip-splitter-lines";
    public static final String KEEP_RECORDS_IF_NUMERIC = "keep-records-if-numeric";
    public static final String DECODE_AS_INTEGER = "decode-as-integer";
    public static final String COLUMN_WIDTHS = "column-widths";
    public static final String COLUMN_COUNT = "column-count";
    public static final String CHARSET = "charset";
    public static final String FUNCTIONS = "functions";

    private CommandLine commandLine;
    private static List<ConsoleTableFunction> functions;
    private List<ConsoleTableFunction> initialisedFunctions;

    public static void main(String[] args) throws Exception {
        CommandLineParser commandLineParser = new DefaultParser();

        try {
            createAllFunctions();

            final CommandLine commandLine = commandLineParser.parse(createOptions(), args);
            validateCommandLine(commandLine);

            final List<ConsoleTableFunction> initialisedFunctions = getInitialisedFunctions(commandLine);

            if (initialisedFunctions.isEmpty()) {
                throw new RuntimeException("No functions defined, please add at least one.");
            }

            DataMungingConsoleApp app = new DataMungingConsoleApp(commandLine, initialisedFunctions);
            app.run();
        }
        catch (ParseException pe) {
            System.err.println("Invalid arguments. " + pe.getMessage());
        }
        catch (RuntimeException re) {
            System.err.println(re.getMessage());
        }
    }

    private static void createAllFunctions() {
        functions = new ArrayList<>();
        functions.add(new FunctionMinDiff());
    }

    private void run() throws Exception {
        StringTable table = loadTable();

        for (ConsoleTableFunction f : initialisedFunctions) {
            System.out.println(f.getValue(table));
        }
    }

    private DataMungingConsoleApp(CommandLine commandLine, List<ConsoleTableFunction> initialisedFunctions) {
        this.commandLine = commandLine;
        this.initialisedFunctions = initialisedFunctions;
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

    private static List<ConsoleTableFunction> getInitialisedFunctions(CommandLine commandLine) {
        List<ConsoleTableFunction> initialisedFunctions = new ArrayList<>();

        List<String> passedFunctions = Arrays.asList(commandLine.getOptionValues(FUNCTIONS));

        functions.stream().filter(f -> passedFunctions.contains(f.getName())).forEach(f -> {
            initialisedFunctions.add(f);
            f.setArguments(commandLine);
        });

        return initialisedFunctions;
    }

    private StringTable loadTable() throws Exception {
        // TODO: this is tight
        StringTableFactory factory = new DefaultStringTableFactoryImpl(new DefaultStringRecordFactoryImpl());
        String filePath = commandLine.getArgs()[0];

        StringRecordParser recParser = getRecParser(filePath);

        StringTable table = null;
        Charset charset = Charset.forName(commandLine.getOptionValue(CHARSET));
        try (StringTableParser parser = factory.createStringTableParser(recParser, Paths.get(filePath).toUri(), charset)) {
            if (commandLine.hasOption(FIRST_LINE_IS_HEADER)) {
                parser.firstRowIsHeader();
            }
            if (commandLine.hasOption(SKIP_EMPTY_LINES)) {
                parser.addLineFilter(factory.getCommonLineFilters().skipEmptyLines());
            }
            if (commandLine.hasOption(SKIP_SPLITTER_LINES)) {
                parser.addLineFilter(factory.getCommonLineFilters().skipSplitterLines());
            }
            if (commandLine.hasOption(KEEP_RECORDS_IF_NUMERIC)) {
                for (String column : commandLine.getOptionValue(KEEP_RECORDS_IF_NUMERIC).split(",")) {
                    parser.addRecordFilter(factory.getCommonRecordFilters().onlyNumbersInColumn(column));
                }
            }
            table = parser.parse();
        }

        if (commandLine.hasOption(DECODE_AS_INTEGER)) {
            table.addStringDecoderToColumns(
                    factory.getCommonDecoders().keepIntegersOnly(),
                    commandLine.getOptionValue(DECODE_AS_INTEGER).split(","));
        }

        return table;
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
        options.addOption(Option.builder().longOpt(COLUMN_WIDTHS).hasArg().argName("COLUMN-WIDTHS").desc("fix widths of the columns in the given file").build());
        options.addOption(Option.builder().longOpt(COLUMN_COUNT).hasArg().argName("NUMBER").desc("expected number of columns").build());
        options.addOption(Option.builder().longOpt(CHARSET).required().hasArg().argName("NAME").desc("name of the charset for the given text file").build());
        options.addOption(Option.builder().longOpt(FUNCTIONS).required().hasArg().argName("FUNC").desc("list of functions to perform").build());

        for (ConsoleTableFunction f : functions) {
            f.initOptions(options);
        }

        return options;
    }
}
