package ru.digitalhabbits.homework2.impl;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import ru.digitalhabbits.homework2.LetterCountMerger;
import ru.digitalhabbits.homework2.LetterCounter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

@AllArgsConstructor
public class LetterCountAndMapMergeTask extends RecursiveTask<Map<Character, Long>> {

    private final List<String> strings;
    private final LetterCounter letterCounter;
    private final LetterCountMerger letterCountMerger;

    @Override
    protected Map<Character, Long> compute() {
        if (strings.size() <= 2) {
            return strings.stream().map(letterCounter::count).reduce(letterCountMerger::merge).orElse(null);
        }

        LetterCountAndMapMergeTask task1 = new LetterCountAndMapMergeTask(strings.subList(0, strings.size() / 2), letterCounter, letterCountMerger);
        LetterCountAndMapMergeTask task2 = new LetterCountAndMapMergeTask(strings.subList(strings.size() / 2, strings.size()), letterCounter, letterCountMerger);

        task1.fork();
        task2.fork();

        return letterCountMerger.merge(task1.join(), task2.join());
    }
}
