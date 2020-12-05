package com.github.dfauth.match;

import com.github.dfauth.trycatch.Try;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.dfauth.match.Matcher._case;
import static com.github.dfauth.match.Matcher.match;
import static com.github.dfauth.match.Matchers.instanceOf;
import static com.github.dfauth.match.PartialFunction.identity;
import static org.junit.Assert.assertEquals;

public class UnitTest {

    private static final Logger logger = LoggerFactory.getLogger(UnitTest.class);

    @Test
    public void testSimplified() throws Throwable {
        Try<Integer> test = Try.success(1);
        assertEquals(test, match(test).using(
                _case(identity())
                ).orElseThrow(() -> new RuntimeException(""))
        );
    }

    @Test
    public void testIt() {
        Try<Integer> test = Try.success(1);
        assertEquals(1, match(test).using(
                _case(instanceOf(Try.Success.class)
                        .thenMap(s -> s.result()))
                ).orElse(0)
        );
    }

    @Test
    public void testAndIf() {
        {
            Try<Integer> test = Try.success(1);
            assertEquals("ONE", match(test).using(
                    _case(instanceOf((Try.Success<Integer> a) -> a)
                            .andIf(s -> s.result() == 1))
                            .thenMap(s -> "ONE"))
                    .orElse("NO_MATCH")
            );
        }
        {
            Try<Integer> test = Try.success(0);
            assertEquals("NO_MATCH", match(test).using(
                    _case(instanceOf((Try.Success<Integer> a) -> a)
                            .andIf(s -> s.result() == 1)
                            .thenMap(s -> "ONE"))
                    ).orElse("NO_MATCH")
            );
        }
        {
            Try<String> test = Try.success("poo");
            assertEquals("NO_MATCH", match(test).using(
                    _case(instanceOf((Try.Success<Integer> a) -> a)
                            .andIf(s -> s.result() == 1)
                            .thenMap(s -> "ONE"))
                    ).orElse("NO_MATCH")
            );
        }
        {
            Try<String> test = Try.success("poo");
            assertEquals("MATCH_POO", match(test).using(
                    _case(instanceOf((Try.Success<Integer> a) -> a)
                            .andIf(s -> s.result() == 1)
                            .thenMap(s -> "ONE")),
                    _case(instanceOf((Try.Success<String> a) -> a)
                            .andIf(s -> s.result().equals("blah"))
                            .andIf(s -> s.result().equalsIgnoreCase("BLAH"))
                            .thenMap(s -> "MATCH_BLAH")),
                    _case(instanceOf((Try.Success<String> a) -> a)
                            .andIf(s -> s.result().equals("poo"))
                            .thenMap(s -> "MATCH_POO"))
                    ).orElse("NO_MATCH")
            );
        }
    }
}
