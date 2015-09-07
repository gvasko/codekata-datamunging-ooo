package hu.gvasko.bootstrap.factory;

import hu.gvasko.testutils.categories.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.*;

import static org.mockito.Mockito.*;

import static hu.gvasko.bootstrap.factory.ABFixture.*;
import static hu.gvasko.bootstrap.factory.DiamondFixture.*;

/**
 * Created by gvasko on 2015.09.07..
 */
@Category(UnitTest.class)
public class DefaultFactoryLoaderTest {

    // avoid infinite recursive loop?

//    static class ReflectionLoader implements RequiredLoader {
//
//        Map<Class<?>, Class<?>> ifaceToClass = new HashMap<>();
//
//        <T> void registerImplementation(Class<T> iface, Class<? extends T> clazz) {
//            ifaceToClass.put(iface, clazz);
//        }
//
//        @Override
//        public <T> List<T> loadServices(Class<T> iface) {
//            if (ifaceToClass.containsKey(iface)) {
//                T impl = (T)ifaceToClass.get(iface);
//            }
//            return null;
//        }
//    }

    @Test
    public void given_twoFactories_when_oneDependsOnAnother_then_dependencyLoaded() {
        RequiredLoader mockLoader = mock(RequiredLoader.class);

        final TestA fakeAImpl = mock(TestA.class);
        when(mockLoader.loadServices(TestA.class)).thenReturn(Arrays.asList(fakeAImpl));
        final TestB fakeBImpl = new TestBImpl();
        when(mockLoader.loadServices(TestB.class)).thenReturn(Arrays.asList(fakeBImpl));

        FactoryLoader sutLoader = new DefaultFactoryLoaderImpl(mockLoader);

        final TestA loadedTestA = sutLoader.loadFactory(TestA.class);
        Assert.assertEquals("SUT Loader, TestA", loadedTestA, fakeAImpl);
        final TestB loadedTestB = sutLoader.loadFactory(TestB.class);
        Assert.assertEquals("SUT Loader, TestB", loadedTestB, fakeBImpl);

        Assert.assertEquals("TestB got TestA", fakeAImpl, loadedTestB.getDependency());
    }

    @Test
    public void given_4Factories_when_diamond_then_allLoadedOnce() {
        RequiredLoader mockLoader = mock(RequiredLoader.class);

        final TestBottom fakeBottomImpl = mock(TestBottom.class);
        when(mockLoader.loadServices(TestBottom.class)).thenReturn(Arrays.asList(fakeBottomImpl));
        final TestLeft fakeLeftImpl = new TestLeftImpl();
        when(mockLoader.loadServices(TestLeft.class)).thenReturn(Arrays.asList(fakeLeftImpl));
        final TestRight fakeRightImpl = new TestRightImpl();
        when(mockLoader.loadServices(TestRight.class)).thenReturn(Arrays.asList(fakeRightImpl));
        final TestTop fakeTopImpl = new TestTopImpl();
        when(mockLoader.loadServices(TestTop.class)).thenReturn(Arrays.asList(fakeTopImpl));
    }
}
