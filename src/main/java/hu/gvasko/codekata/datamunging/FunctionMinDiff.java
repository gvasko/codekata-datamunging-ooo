package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringrecord.StringRecord;
import hu.gvasko.stringtable.StringTable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gvasko on 2015.09.30..
 */
public class FunctionMinDiff implements ConsoleTableFunction {

    public static final String FUNCTION_NAME_MINDIFF = "MinDiff";

    public static final String DIFF_1_COLUMN = "diff-1";
    public static final String DIFF_2_COLUMN = "diff-2";
    public static final String DIFF_RETURN_COLUMN = "diff-return";

    private Map<String, String> arguments;

    public FunctionMinDiff() {
        arguments = new HashMap<>();
    }

    @Override
    public String getName() {
        return FUNCTION_NAME_MINDIFF;
    }

    @Override
    public void initOptions(Options options) {
        options.addOption(Option.builder().longOpt(DIFF_1_COLUMN).required(false).hasArg().argName("COLUMN-NAME").desc("one column name to to calculate difference").build());
        options.addOption(Option.builder().longOpt(DIFF_2_COLUMN).required(false).hasArg().argName("COLUMN-NAME").desc("another column name to calculate difference").build());
        options.addOption(Option.builder().longOpt(DIFF_RETURN_COLUMN).required(false).hasArg().argName("COLUMN-NAME").desc("the program returns the value belonging to this column in the record where the difference is minimal").build());
    }

    @Override
    public void setArguments(CommandLine commandLine) {
        if (commandLine.hasOption(DIFF_1_COLUMN)) {
            arguments.put(DIFF_1_COLUMN, commandLine.getOptionValue(DIFF_1_COLUMN));
        }
        if (commandLine.hasOption(DIFF_2_COLUMN)) {
            arguments.put(DIFF_2_COLUMN, commandLine.getOptionValue(DIFF_2_COLUMN));
        }
        if (commandLine.hasOption(DIFF_RETURN_COLUMN)) {
            arguments.put(DIFF_RETURN_COLUMN, commandLine.getOptionValue(DIFF_RETURN_COLUMN));
        }

        validateArguments(arguments);
    }

    @Override
    public String getValue(StringTable table) {
        StringRecord actualRecord = DataMungingUtil.getFirstMinDiffRecord(
                table.getAllRecords(), arguments.get(DIFF_1_COLUMN), arguments.get(DIFF_2_COLUMN));

        return actualRecord.get(arguments.get(DIFF_RETURN_COLUMN));
    }

    private void validateArguments(Map<String, String> arguments) {
        if (!arguments.containsKey(DIFF_1_COLUMN)) {
            throw new IllegalArgumentException("Argument not passed: " + DIFF_1_COLUMN);
        }

        if (!arguments.containsKey(DIFF_2_COLUMN)) {
            throw new IllegalArgumentException("Argument not passed: " + DIFF_2_COLUMN);
        }

        if (!arguments.containsKey(DIFF_RETURN_COLUMN)) {
            throw new IllegalArgumentException("Argument not passed: " + DIFF_RETURN_COLUMN);
        }
    }
}
