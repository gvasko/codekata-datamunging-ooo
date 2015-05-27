package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Before;
import org.junit.experimental.categories.Category;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gvasko on 2015.05.27..
 */
@Category(UnitTest.class)
public class TableParserLogicTest {

    private TableParserLogic sutTableParserLogic;
    private StringTableBuilderFactory testDoubleTableBuilderFactory;
    private StringRecordParser testDoubleRecParser;
    private boolean isFirstRowHeader;
    private List<Predicate<String>> lineFilters;
    private List<Predicate<StringRecord>> recordFilters;

    @Before
    public void given() {

    }



}
