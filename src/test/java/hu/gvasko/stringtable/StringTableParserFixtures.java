package hu.gvasko.stringtable;

/**
 * Created by Gvasko on 2015.05.08..
 */
public class StringTableParserFixtures {

    public static final String emptyText = "";

    public static final String spaceText = "   ";
    private static int[] defaultHeader = { 5, 5, 5 };

    public static String[] getDefaultSchema() {
        return new String[] { "Aaa", "Bbb", "Ccc" };
    }
    public static String[] getNumberedSchema() {
        return DefaultFactory.getDefaultHeader(defaultHeader.length);
    }

    public static final StringRecordParser getDefaultRecordParser() {
        return DefaultFactory.getInstance().getFixWidthRecordParser(defaultHeader);
    }

    public static final String defaultText = ""
            + " Aaa  Bbb  Ccc \n"
            + "               \n"   // empty row (filled with space)
            + "   0   b0   c0 \n"
            + "   1   b1   c1 \n"
            + "   2        c2 \n"   // empty cell b2
            + "   3   b3\n"         // empty cell c3
            + "---------------\n"   // this is considered as last row
            + "         \n";        // two empty rows at the end


    public static String[][] getTable_firstRowIsHeader_skipEmptyLines_onlyNumbersInFirstColumn() {
        return new String[][] {
                { "0", "b0", "c0" },
                { "1", "b1", "c1" },
                { "2", "", "c2" },
                { "3", "b3", "" }
        };
    }

    public static String[][] getTable_skipEmptyLines_skipSplitterLines() {
        return new String[][] {
                { "Aaa", "Bbb", "Ccc" },
                { "0", "b0", "c0" },
                { "1", "b1", "c1" },
                { "2", "", "c2" },
                { "3", "b3", "" }
        };
    }

    public static String[][] getTable_skipEmptyLines() {
        return new String[][] {
                { "Aaa", "Bbb", "Ccc" },
                { "0", "b0", "c0" },
                { "1", "b1", "c1" },
                { "2", "", "c2" },
                { "3", "b3", "" },
                { "-----", "-----", "-----" }
        };
    }

    public static String[][] getTable_full() {
        return new String[][] {
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

}
