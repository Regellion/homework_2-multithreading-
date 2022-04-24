package ru.digitalhabbits.homework2.impl;

import lombok.AllArgsConstructor;
import ru.digitalhabbits.homework2.FileLetterCounter;
import ru.digitalhabbits.homework2.FileReader;
import ru.digitalhabbits.homework2.LetterCountMerger;
import ru.digitalhabbits.homework2.LetterCounter;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@AllArgsConstructor
public class AsyncFileLetterCounter implements FileLetterCounter {

    private final FileReader fileReader;
    private final LetterCounter letterCounter;
    private final LetterCountMerger letterCountMerger;
    @Override
    public Map<Character, Long> count(File input) {
        if(input == null) {
            return Collections.emptyMap();
        }
        List<String> strings = fileReader.readLines(input);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        return forkJoinPool.invoke(new LetterCountAndMapMergeTask(strings, letterCounter, letterCountMerger));
    }
}
