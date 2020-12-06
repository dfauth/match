package com.github.dfauth.match;

import java.util.function.Function;

public class Matchers {

    public static <T,R extends T> Case<T,R> downcast(Downcaster<T,R> f) {
        return new Case(new PartialFunction<T, R>() {
            @Override
            public R apply(T t) {
                return f.apply(t);
            }

            @Override
            public boolean test(T t) {
                try {
                    apply(t);
                    return true;
                } catch (ClassCastException e) {
                    return false;
                }
            }
        });
    }

    @FunctionalInterface
    interface Downcaster<T,R extends T> extends Function<T,R> {
        @Override
        default R apply(T t) {
            return downcast(t);
        }

        R downcast(T t);
    }
}
