package hu.gvasko.stringrecord.defaultimpl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import hu.gvasko.stringrecord.MainRecordFactory;
import hu.gvasko.stringrecord.StringRecordBuilder;
import hu.gvasko.stringrecord.StringRecordBuilderFactory;

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
                bind(StringRecordFactory.class).to(DefaultStringRecordImpl.FactoryImpl.class);
                bind(StringRecordBuilderFactory.class).to(DefaultStringRecordBuilderImpl.FactoryImpl.class);
            }
        };
    }

    public Module getGuiceModule() {
        return module;
    }

    public StringRecordBuilder newStringRecordBuilder() {
        return newStringRecordBuilderFactory().createNew();
    }

    @Override
    public StringRecordBuilderFactory newStringRecordBuilderFactory() {
        return injector.getInstance(StringRecordBuilderFactory.class);
    }

}
