package hu.gvasko.bootstrap.factory;

/**
 * Created by gvasko on 2015.09.07..
 */
public interface FactoryLoader {
    <F extends BaseFactory> F loadFactory(Class<F> factoryInterface);
}
