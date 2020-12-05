package com.github.dfauth.match;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PartialFunction<I,O> extends Function<I,O>, Predicate<I> {

    default boolean isDefinedAt(I i) {
        return test(i);
    }

    static <T> PartialFunction<T, T> identity() {
        return new PartialFunction<T, T>() {
            @Override
            public boolean test(T t) {
                return true;
            }

            @Override
            public T apply(T t) {
                return t;
            }
        };
    }

    default <V> PartialFunction<I, V> thenMap(Function<? super O, ? extends V> after) {
        return new PartialFunction<I, V>() {
            @Override
            public V apply(I i) {
                return after.apply(PartialFunction.this.apply(i));
            }

            @Override
            public boolean test(I i) {
                return PartialFunction.this.test(i);
            }
        };
    }

    static <T,R> PartialFunction<T,R> wrap(Function<T,R> f) {
        return new PartialFunction<T,R>() {
            @Override
            public boolean test(T t) {
                return true;
            }

            @Override
            public R apply(T t) {
                return f.apply(t);
            }
        };
    }
}
