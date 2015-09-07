package hu.gvasko.bootstrap.factory;

import java.util.List;

/**
 * Created by gvasko on 2015.09.07..
 */
public class DefaultFactoryLoaderImpl implements FactoryLoader {

//    private static DefaultFactoryLoaderImpl soleInstance;
//
//    public static FactoryLoader getSoleInstance() {
//        // TODO: concurrent access
//        if (soleInstance != null) {
//            soleInstance = new DefaultFactoryLoaderImpl();
//        }
//        return soleInstance;
//    }

    private RequiredLoader loader;

    public DefaultFactoryLoaderImpl() {
        this(new ServiceLoaderWrapperImpl());
    }

    public DefaultFactoryLoaderImpl(RequiredLoader loader) {
        this.loader = loader;
    }

    @Override
    public <F extends BaseFactory> F loadFactory(Class<F> factoryInterface) {
        List<F> factories = loader.loadServices(factoryInterface);

        if (factories.isEmpty()) {
            return null;
        }
        else if (factories.size() == 1) {
            final F candidate = factories.get(0);
            candidate.loadDependencies(this);
            return candidate;
        }
        else {
            throw new RuntimeException("Multiple factory implementations found.");
        }
    }
}
