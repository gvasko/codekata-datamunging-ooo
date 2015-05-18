package hu.gvasko.stringtable;

import org.junit.Test;

/**
 * Created by Gvasko on 2015.05.18..
 */
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
