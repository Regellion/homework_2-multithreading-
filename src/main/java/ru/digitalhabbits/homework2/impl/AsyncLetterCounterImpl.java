package ru.digitalhabbits.homework2.impl;

import ru.digitalhabbits.homework2.LetterCounter;

import java.util.HashMap;
import java.util.Map;

public class AsyncLetterCounterImpl implements LetterCounter {
    @Override
    public Map<Character, Long> count(String input) {
        Map<Character, Long> charMap = new HashMap<>();
        input.chars()
                .forEach(c -> {
                    charMap.computeIfAbsent((char) c, (k) -> 0L);
                    charMap.computeIfPresent((char) c, (k, v) -> v + 1);
                });
        return charMap;
    }
}
