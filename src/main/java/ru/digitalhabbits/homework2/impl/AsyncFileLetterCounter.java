package ru.digitalhabbits.homework2.impl;

import ru.digitalhabbits.homework2.FileLetterCounter;
import ru.digitalhabbits.homework2.FileReader;
import ru.digitalhabbits.homework2.LetterCountMerger;
import ru.digitalhabbits.homework2.LetterCounter;

import java.io.File;
import java.util.Map;

public class AsyncFileLetterCounter implements FileLetterCounter {

    private final FileReader fileReader = new FileReaderImpl();
    private final LetterCounter letterCounter = new AsyncLetterCounterImpl();
    private final LetterCountMerger letterCountMerger = new LetterCountMergerImpl();

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
