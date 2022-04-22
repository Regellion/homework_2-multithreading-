package ru.digitalhabbits.homework2.impl;

import ru.digitalhabbits.homework2.FileLetterCounter;
import ru.digitalhabbits.homework2.FileReader;
import ru.digitalhabbits.homework2.LetterCountMerger;
import ru.digitalhabbits.homework2.LetterCounter;

import java.io.File;
import java.util.Map;

public class AsyncFileLetterCounter implements FileLetterCounter {

    private final FileReader fileReader;
    private final LetterCounter letterCounter;
    private final LetterCountMerger letterCountMerger;

    public AsyncFileLetterCounter(FileReader fileReader, LetterCounter letterCounter, LetterCountMerger letterCountMerger) {
        this.fileReader = fileReader;
        this.letterCounter = letterCounter;
        this.letterCountMerger = letterCountMerger;
    }

    @Override
    public Map<Character, Long> count(File input) {
        return fileReader
                .readLines(input)
                .parallel()
                .map(letterCounter::count)
                .reduce(letterCountMerger::merge)
                .orElse(null);

    }
}
