package hu.gvasko.stringrecord.defaultimpl;

import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordFactory;
import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by gvasko on 2015.05.22..
 */
@Category(UnitTest.class)
public class StringRecordBuilderTest {

    private StringRecordBuilder recBuilder;
    private StringRecordFactoryExt spyFactory;

    @Before
    public void given() {
        // TODO: why spy() cannot be used here?
        // http://xunitpatterns.com/Test%20Spy.html
        // "capture the indirect output calls made to another component
        // by the system under test (SUT) for later verification by the test"
        spyFactory = mock(StringRecordFactoryExt.class);
        recBuilder = new DefaultStringRecordBuilderImpl(spyFactory);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_ZeroFieldIsAdded_then_DoesNotCreateRecord_ThrowsException() {
        try {
            recBuilder.build();
            Assert.fail("Should throw exception.");
        }
        catch (IllegalStateException ex) {
            verify(spyFactory, never()).createStringRecord(anyMap());
            verifyZeroInteractions(spyFactory);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_DuplicateColumnNameAdded_then_DoesNotCreateRecord_ThrowsException() {
        try {
            recBuilder.addField("AA", "a");
            recBuilder.addField("AA", "aa");
            Assert.fail("Should throw exception.");
        }
        catch (IllegalArgumentException ex) {
            verify(spyFactory, never()).createStringRecord(anyMap());
            verifyZeroInteractions(spyFactory);
        }
    }

    @Test
    public void when_FieldsAddedNormally_then_CreatesRecordUsingAllPassedFields() {
        addDefaultFieldsABC();
        recBuilder.build();
        verify(spyFactory).createStringRecord(eq(getDefaultExpectedMapABC()));
    }

    private void andGiven_BuilderHasAlreadyCreatedRecord() {
        addDefaultFieldsABC();
        recBuilder.build();
    }

    @Test
    public void when_BuilderIsCalledAgain_then_CreatesRecordUsingAllPreviouslyAndNewlyPassedFields() {
        andGiven_BuilderHasAlreadyCreatedRecord();

        Map<String, String> expectedMap = getDefaultExpectedMapABC();

        verify(spyFactory).createStringRecord(eq(expectedMap));

        recBuilder.addField("DD", "dd");
        recBuilder.build();

        expectedMap.put("DD", "dd");
        verify(spyFactory, times(2)).createStringRecord(eq(expectedMap));
    }

    @Test
    public void when_ZeroMultipleFieldAdded_then_NothingHappens() {
        addDefaultFieldsABC();
        recBuilder.addFields(new String[0], new String[0]);
        recBuilder.build();
        verify(spyFactory).createStringRecord(eq(getDefaultExpectedMapABC()));
    }

    @Test
    public void when_MultipleFieldsAddedNormally_then_CreatesRecordUsingAllPassedFields() {
        recBuilder.addFields(new String[] {"AA", "BB", "CC"}, new String[] {"aa", "bb", "cc"});
        recBuilder.build();
        verify(spyFactory).createStringRecord(eq(getDefaultExpectedMapABC()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_MultipleFieldsAddDuplicate_then_DoesNotCreateRecord_ThrowsException() {
        try {
            recBuilder.addFields(new String[]{"AA", "AA"}, new String[]{"a", "aa"});
            Assert.fail("Should throw exception.");
        }
        catch (IllegalArgumentException ex) {
            verify(spyFactory, never()).createStringRecord(anyMap());
            verifyZeroInteractions(spyFactory);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_MultipleFieldsInconsistent_then_DoesNotCreateRecord_ThrowsException() {
        try {
            recBuilder.addFields(new String[]{"AA", "BB"}, new String[]{"aa", "bb", "cc"});
            Assert.fail("Should throw exception.");
        }
        catch (IllegalArgumentException ex) {
            verify(spyFactory, never()).createStringRecord(anyMap());
            verifyZeroInteractions(spyFactory);
        }
    }

    private void addDefaultFieldsABC() {
        recBuilder.addField("AA", "aa");
        recBuilder.addField("BB", "bb");
        recBuilder.addField("CC", "cc");
    }

    private Map<String, String> getDefaultExpectedMapABC() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("AA", "aa");
        expectedMap.put("BB", "bb");
        expectedMap.put("CC", "cc");
        return expectedMap;
    }


}
