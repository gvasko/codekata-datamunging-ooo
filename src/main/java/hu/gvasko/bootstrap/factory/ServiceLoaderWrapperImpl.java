package hu.gvasko.bootstrap.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by gvasko on 2015.09.07..
 */
class ServiceLoaderWrapperImpl implements RequiredLoader {
    @Override
    public <T> List<T> loadServices(Class<T> iface) {
        ServiceLoader<T> loader = ServiceLoader.load(iface);
        List<T> services = new ArrayList<>();
        for (T service : loader) {
            services.add(service);
        }
        return services;
    }
}
