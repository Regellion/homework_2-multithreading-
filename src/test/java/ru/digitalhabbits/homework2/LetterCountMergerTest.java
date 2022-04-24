package ru.digitalhabbits.homework2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.digitalhabbits.homework2.impl.LetterCountMergerImpl;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class LetterCountMergerTest {

    private static LetterCountMerger letterCountMerger;
    private static final Map<Character, Long> first = Map.of('c', 1L, 'a', 2L);
    private static final Map<Character, Long> second = Map.of('c', 2L, 'a', 1L, 'b', 3L);
    private static final Map<Character, Long> empty = Collections.emptyMap();

    @BeforeAll
    static void init() {
        letterCountMerger = new LetterCountMergerImpl();
    }

    @Test
    void merge() {
        Map<Character, Long> result = letterCountMerger.merge(first, second);

        assertThat(result).containsOnly(
                entry('a', 3L),
                entry('b', 3L),
                entry('c', 3L)
        );
    }

    @Test
    void merge_first_is_empty() {
        Map<Character, Long> result = letterCountMerger.merge(empty, second);

        assertThat(result).containsOnly(
                entry('a', 1L),
                entry('b', 3L),
                entry('c', 2L)
        );
    }

    @Test
    void merge_second_is_empty() {
        Map<Character, Long> result = letterCountMerger.merge(first, empty);

        assertThat(result).containsOnly(
                entry('a', 2L),
                entry('c', 1L)
        );
    }

    @Test
    void merge_all_arguments_is_empty() {
        Map<Character, Long> result = letterCountMerger.merge(empty, empty);

        assertThat(result).isEmpty();
    }

    @Test
    void merge_first_is_null() {
        Map<Character, Long> result = letterCountMerger.merge(null, second);

        assertThat(result).containsOnly(
                entry('a', 1L),
                entry('b', 3L),
                entry('c', 2L)
        );
    }

    @Test
    void merge_second_is_null() {
        Map<Character, Long> result = letterCountMerger.merge(first, null);

        assertThat(result).containsOnly(
                entry('a', 2L),
                entry('c', 1L)
        );
    }

    @Test
    void merge_all_arguments_is_null() {
        Map<Character, Long> result = letterCountMerger.merge(null, null);

        assertThat(result).isEmpty();
    }
}