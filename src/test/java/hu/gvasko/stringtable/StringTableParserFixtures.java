package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
public class StringTableParserFixtures {

    public static final String[] defaultSchema = { "Aaa", "Bbb", "Ccc" };
    public static final String[] numberedSchema = StringTableFactory.getDefaultHeader(defaultSchema.length);
    public static final int[] defaultHeader = { 5, 5, 5 };

    public static final String emptyText = "";
    public static final String spaceText = "   ";

    public static final String defaultText = ""
            + " Aaa  Bbb  Ccc \n"
            + "               \n"   // empty row (filled with space)
            + "  a0   b0   c0 \n"
            + "  a1   b1   c1 \n"
            + "  a2        c2 \n"   // empty cell b2
            + "  a3   b3\n"         // empty cell c3
            + "---------------\n"   // this is considered as last row
            + "         \n";        // two empty rows at the end


    public static final String[][] table_firstRowIsHeader_excludeLastRow_excludeEmptyRows = {
            { "a0", "b0", "c0" },
            { "a1", "b1", "c1" },
            { "a2", "", "c2" },
            { "a3", "b3", "" }
    };

    public static final String[][] table_excludeLastRow_excludeEmptyRows = {
            { "Aaa", "Bbb", "Ccc" },
            { "a0", "b0", "c0" },
            { "a1", "b1", "c1" },
            { "a2", "", "c2" },
            { "a3", "b3", "" }
    };

    public static final String[][] table_excludeEmptyRows = {
            { "Aaa", "Bbb", "Ccc" },
            { "a0", "b0", "c0" },
            { "a1", "b1", "c1" },
            { "a2", "", "c2" },
            { "a3", "b3", "" },
            { "-----", "-----", "-----" }
    };

    public static final String[][] table_full = {
            { "Aaa", "Bbb", "Ccc" },
            { "", "", "" },
            { "a0", "b0", "c0" },
            { "a1", "b1", "c1" },
            { "a2", "", "c2" },
            { "a3", "b3", "" },
            { "-----", "-----", "-----" },
            { "", "", "" }
    };

}
