package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
public class StringTableParserFixtures {

    public static final String[] defaultSchema = { "Aaa", "Bbb", "Ccc" };
    public static final String[] numberedSchema = StringTableFactory.getDefaultHeader(defaultSchema.length);
    public static final int[] defaultHeader = { 5, 5, 5 };
    public static final StringRecordParser defaultRecordParser = StringTableFactory.getInstance().getFixWidthRecordParser(defaultHeader);

    public static final String emptyText = "";
    public static final String spaceText = "   ";

    public static final String defaultText = ""
            + " Aaa  Bbb  Ccc \n"
            + "               \n"   // empty row (filled with space)
            + "   0   b0   c0 \n"
            + "   1   b1   c1 \n"
            + "   2        c2 \n"   // empty cell b2
            + "   3   b3\n"         // empty cell c3
            + "---------------\n"   // this is considered as last row
            + "         \n";        // two empty rows at the end


    public static final String[][] table_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn = {
            { "0", "b0", "c0" },
            { "1", "b1", "c1" },
            { "2", "", "c2" },
            { "3", "b3", "" }
    };

    public static final String[][] table_skipEmptyLines_skipSplitterLines = {
            { "Aaa", "Bbb", "Ccc" },
            { "0", "b0", "c0" },
            { "1", "b1", "c1" },
            { "2", "", "c2" },
            { "3", "b3", "" }
    };

    public static final String[][] table_skipEmptyLines = {
            { "Aaa", "Bbb", "Ccc" },
            { "0", "b0", "c0" },
            { "1", "b1", "c1" },
            { "2", "", "c2" },
            { "3", "b3", "" },
            { "-----", "-----", "-----" }
    };

    public static final String[][] table_full = {
            { "Aaa", "Bbb", "Ccc" },
            { "", "", "" },
            { "0", "b0", "c0" },
            { "1", "b1", "c1" },
            { "2", "", "c2" },
            { "3", "b3", "" },
            { "-----", "-----", "-----" },
            { "", "", "" }
    };

}
