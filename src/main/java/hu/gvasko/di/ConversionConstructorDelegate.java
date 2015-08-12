package hu.gvasko.di;

/**
 * Created by gvasko on 2015.08.12..
 */
public interface ConversionConstructorDelegate<T, R> {
    R call(T source);
}
