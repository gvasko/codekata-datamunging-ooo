package hu.gvasko.bootstrap.factory;

/**
 * Created by gvasko on 2015.09.07..
 */
public class DiamondFixture {

    interface TestTop extends BaseFactory {
        TestLeft getLeftDependency();
        TestRight getRightDependency();
    }

    interface TestLeft extends BaseFactory {
        TestBottom getDependency();
    }

    interface TestRight extends BaseFactory {
        TestBottom getDependency();
    }

    interface TestBottom extends BaseFactory {
    }

    static class TestTopImpl implements TestTop {

        TestLeft left;
        TestRight right;

        @Override
        public TestLeft getLeftDependency() {
            return left;
        }

        @Override
        public TestRight getRightDependency() {
            return right;
        }

        @Override
        public void loadDependencies(FactoryLoader loader) {
            left = loader.loadFactory(TestLeft.class);
            right = loader.loadFactory(TestRight.class);
        }
    }

    static class TestLeftImpl implements TestLeft {

        TestBottom bottom;

        @Override
        public TestBottom getDependency() {
            return bottom;
        }

        @Override
        public void loadDependencies(FactoryLoader loader) {
            bottom = loader.loadFactory(TestBottom.class);
        }
    }

    static class TestRightImpl implements TestRight {

        TestBottom bottom;

        @Override
        public TestBottom getDependency() {
            return bottom;
        }

        @Override
        public void loadDependencies(FactoryLoader loader) {
            bottom = loader.loadFactory(TestBottom.class);
        }
    }


}
