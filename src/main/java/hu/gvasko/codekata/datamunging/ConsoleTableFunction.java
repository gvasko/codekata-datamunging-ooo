package hu.gvasko.codekata.datamunging;

import hu.gvasko.stringtable.StringTable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.Map;

/**
 * Created by gvasko on 2015.09.30..
 */
public interface ConsoleTableFunction {
    String getName();

    void initOptions(Options options);

    void setArguments(CommandLine commandLine);

    String getValue(StringTable table);
}
