package ru.digitalhabbits.homework2.impl;

import ru.digitalhabbits.homework2.LetterCountMerger;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LetterCountMergerImpl implements LetterCountMerger {
    @Override
    public Map<Character, Long> merge(Map<Character, Long> first, Map<Character, Long> second) {
        if (first == null && second == null) {
            return Collections.emptyMap();
        }

        if (first == null || first.isEmpty()) {
            return second;
        }

        if (second == null || second.isEmpty()) {
            return first;
        }

        return Stream
                .concat(first.entrySet().stream(), second.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }
}
