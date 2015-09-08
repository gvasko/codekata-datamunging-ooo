package hu.gvasko.stringtable.defaultimpl;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.function.UnaryOperator;

/**
 * Created by gvasko on 2015.05.07..
 */
@Category(UnitTest.class)
public class KeepIntegerOnlyTest {

    private UnaryOperator<String> keepIntegersOnly;

    @Before
    public void given() {
        keepIntegersOnly = new DefaultStringTableFactoryImpl(null).keepIntegersOnly();
    }

    @Test
    public void when_integer_then_sameReturns() {
        assertThat(keepIntegersOnly.apply("1234"), equalTo("1234"));
    }

    @Test
    public void when_spacesAround_then_returnsTrimmed() {
        assertThat(keepIntegersOnly.apply("  1234  "), equalTo("1234"));
    }

    @Test
    public void when_spacesInside_then_spacesRemoved() {
        assertThat(keepIntegersOnly.apply("12  34"), equalTo("1234"));
    }

    @Test
    public void when_containsLetters_then_lettersRemoved() {
        assertThat(keepIntegersOnly.apply("12Az34"), equalTo("1234"));
    }

    @Test
    public void when_lettersAround_then_lettersRemoved() {
        assertThat(keepIntegersOnly.apply("A1234Z"), equalTo("1234"));
    }

    @Test
    public void when_nonAlphaCharactersAround_then_theyRemoved() {
        assertThat(keepIntegersOnly.apply("++1234**"), equalTo("1234"));
    }

}
