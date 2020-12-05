package com.github.dfauth.match;

import java.util.function.Predicate;

public class Case<T,R> implements PartialFunction<T,R> {

    private final PartialFunction<T,R> nested;
    private Predicate<R> p;

    public Case(PartialFunction<T, R> nested) {
        this.nested = nested;
    }

    public Case<T,R> andIf(Predicate<R> p) {
        this.p = p;
        return this;
    }

    @Override
    public R apply(T t) {
        return nested.apply(t);
    }

    @Override
    public boolean test(T t) {
        try {
            return nested.test(t) && p.test(apply(t));
        } catch (RuntimeException e) {
            return false;
        }
    }
}
