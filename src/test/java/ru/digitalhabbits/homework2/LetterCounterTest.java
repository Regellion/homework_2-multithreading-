package ru.digitalhabbits.homework2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.digitalhabbits.homework2.impl.LetterCounterImpl;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


class LetterCounterTest {
    private static LetterCounter letterCounter;

    @BeforeAll
    static void init() {
        letterCounter = new LetterCounterImpl();
    }

    @Test
    void count() {
        String input = "abcdeabcd";
        Map<Character, Long> count = letterCounter.count(input);
        assertThat(count).containsOnly(
                entry('a', 2L),
                entry('b', 2L),
                entry('c', 2L),
                entry('d', 2L),
                entry('e', 1L)
        );
    }

    @Test
    void count_empty_string() {
        String input = "";
        Map<Character, Long> count = letterCounter.count(input);
        assertThat(count).isEmpty();
    }

    @Test
    void count_null_string() {
        Map<Character, Long> count = letterCounter.count(null);
        assertThat(count).isEmpty();
    }

}