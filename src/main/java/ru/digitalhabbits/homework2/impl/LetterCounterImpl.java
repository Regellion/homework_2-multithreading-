package ru.digitalhabbits.homework2.impl;

import ru.digitalhabbits.homework2.LetterCounter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LetterCounterImpl implements LetterCounter {
    @Override
    public Map<Character, Long> count(String input) {
        if(input == null) {
            return Collections.emptyMap();
        }
        Map<Character, Long> result = new HashMap<>();
        input.chars()
                .forEach(c-> result.compute((char) c, (k, v) -> v == null ? 1L : ++v));
        return result;
    }
}
