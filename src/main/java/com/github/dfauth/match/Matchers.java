package com.github.dfauth.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static com.github.dfauth.match.PartialFunction.wrap;

public class Matchers {

    private static final Logger logger = LoggerFactory.getLogger(Matchers.class);

    public static <T,R extends T> Case<T,R> instanceOf(Function<T,R> f) {
        return new Case(wrap(f));
    }

    public static <T,R extends T> Case<T,R> instanceOf(Class<R> classOfR) {
        return new Case(new PartialFunction<T, R>() {
            @Override
            public boolean test(T t) {
                return classOfR.isAssignableFrom(t.getClass());
            }

            @Override
            public R apply(T t) {
                return classOfR.cast(t);
            }
        });
    }
}
