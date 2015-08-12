package hu.gvasko.stringrecord.defaultimpl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import hu.gvasko.stringrecord.MainRecordFactory;
import hu.gvasko.stringrecord.StringRecordBuilderConstructorDelegate;

/**
 * Created by gvasko on 2015.06.01..
 */
public class DefaultMainRecordFactoryImpl implements MainRecordFactory {

    private Module module;
    private Injector injector;

    public DefaultMainRecordFactoryImpl() {
        module = createGuiceModule();
        injector = Guice.createInjector(module);
    }

    public static Module createGuiceModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                // TODO: ONLY FACTORIES???
                // TODO: is guice really necessary here?
                bind(StringRecordConstructorDelegate.class).to(DefaultStringRecordImpl.ConstructorImpl.class);
                bind(StringRecordBuilderConstructorDelegate.class).to(DefaultStringRecordBuilderImpl.ConstructorDelegateImpl.class);
            }
        };
    }

    public Module getGuiceModule() {
        return module;
    }

    @Override
    public StringRecordBuilderConstructorDelegate getStringRecordBuilderConstructor() {
        return injector.getInstance(StringRecordBuilderConstructorDelegate.class);
    }

}
