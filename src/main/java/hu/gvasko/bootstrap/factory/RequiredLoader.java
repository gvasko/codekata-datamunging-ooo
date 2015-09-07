package hu.gvasko.bootstrap.factory;

import java.util.List;

/**
 * Created by gvasko on 2015.09.07..
 */
interface RequiredLoader {
    <T> List<T> loadServices(Class<T> iface);
}
