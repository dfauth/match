package com.github.dfauth.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Matcher<T> {

    private static final Logger logger = LoggerFactory.getLogger(com.github.dfauth.match.Matcher.class);
    private T target;

    public Matcher(T target) {
        this.target = target;
    }

    public static final <T> Matcher<T> match(T t) {
        return new Matcher(t);
    }

    public static final <T,R> PartialFunction<T,R> _case(PartialFunction<T,R> p) {
        return p;
    }

    public static final <T> PartialFunction<T,T> _case(Predicate<T> p) {
        return new PartialFunction<T,T>(){
            @Override
            public boolean test(T t) {
                return p.test(t);
            }

            @Override
            public T apply(T t) {
                return t;
            }
        };
    }

    public <R> Optional<R> using(PartialFunction<T,R>... cases) {
        return Stream.of(cases).filter(p -> p.isDefinedAt(this.target)).map(p -> p.apply(this.target)).findFirst();
    }

}
