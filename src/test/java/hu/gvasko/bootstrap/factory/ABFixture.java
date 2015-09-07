package hu.gvasko.bootstrap.factory;

/**
 * Created by gvasko on 2015.09.07..
 */
public class ABFixture {

    interface TestA extends BaseFactory {
    }

    interface TestB extends BaseFactory {
        TestA getDependency();
    }

    static class TestBImpl implements TestB {

        TestA a;

        @Override
        public TestA getDependency() {
            return a;
        }

        @Override
        public void loadDependencies(FactoryLoader loader) {
            a = loader.loadFactory(TestA.class);
        }
    }

}
