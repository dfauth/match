package com.github.dfauth.match;

import java.util.Optional;
import java.util.function.Predicate;

public class Case<T,R> implements PartialFunction<T,R> {

    private final PartialFunction<T,R> nested;
    private Optional<Predicate<R>> optP = Optional.empty();

    public Case(PartialFunction<T, R> nested) {
        this.nested = nested;
    }

    public Case<T,R> andIf(Predicate<R> p) {
        this.optP = Optional.ofNullable(p);
        return this;
    }

    @Override
    public R apply(T t) {
        return nested.apply(t);
    }

    @Override
    public boolean test(T t) {
        try {
            return optP.map(p -> p.test(apply(t))).orElse(true) && nested.test(t);
        } catch (RuntimeException e) {
            return false;
        }
    }
}
