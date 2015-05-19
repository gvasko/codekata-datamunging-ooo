package hu.gvasko.stringtable;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created by Gvasko on 2015.05.18..
 */
@Category(UnitTest.class)
public class Dummy {

    // TODO: move these tests to the right place

    @Test(expected = IllegalStateException.class)
    public void emptyRecordCannotBeCreatedThrowsException() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        stringTableFactory.newStringRecordBuilder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void schemaMustBeUnique() {
        StringTableFactory stringTableFactory = StringTableFactory.getInstance();
        stringTableFactory.newStringTableBuilderFactory().createNew(new String[]{"A", "B", "A"});
    }


}
