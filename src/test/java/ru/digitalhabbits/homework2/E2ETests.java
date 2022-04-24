package ru.digitalhabbits.homework2;

import static com.google.common.io.Resources.getResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.digitalhabbits.homework2.impl.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class E2ETests {

    private static FileReader fileReader;
    private static FileReader fileReaderMock;
    private static LetterCounter letterCounter;
    private static LetterCountMerger letterCountMerger;

    @BeforeAll
    static void init() {
        fileReader = new FileReaderImpl();
        fileReaderMock = new FileReaderMockImpl();
        letterCounter = new LetterCounterImpl();
        letterCountMerger = new LetterCountMergerImpl();
    }


    @Test
    void async_file_letter_counting_should_return_predicted_count() {
        var file = getFile("test.txt");
        var counter = new AsyncFileLetterCounter(fileReader, letterCounter, letterCountMerger);

        Map<Character, Long> count = counter.count(file);

        assertThat(count).containsOnly(
                entry('a', 2697L),
                entry('b', 2683L),
                entry('c', 2647L),
                entry('d', 2613L),
                entry('e', 2731L),
                entry('f', 2629L)
        );
    }


    @Test
    void file_reader_work_in_main_thread() {
        var file = getFile("test.txt");
        FileReader fileReader = new FileReaderMockImpl();

        long start = System.currentTimeMillis();
        fileReader.readLines(file);
        long end = System.currentTimeMillis();

        assertThat(end - start).isGreaterThan(1000);
    }

    @Test
    void async_letter_counter_work_in_different_threads() {
        var file = getFile("test.txt");

        long start = System.currentTimeMillis();
        Map<Character, Long> count = new AsyncFileLetterCounterMock(fileReaderMock, letterCounter, letterCountMerger).count(file);
        long end = System.currentTimeMillis();

        assertThat(end - start).isLessThan(10000);
        assertThat(count).containsOnly(
                entry('a', 2697L),
                entry('b', 2683L),
                entry('c', 2647L),
                entry('d', 2613L),
                entry('e', 2731L),
                entry('f', 2629L)
        );
    }

    private File getFile(String name) {
        return new File(getResource(name).getPath());
    }

    public static class FileReaderMockImpl implements FileReader {
        @Override
        public List<String> readLines(File file) {
            try {
                Thread.sleep(1000);
                return java.nio.file.Files.readAllLines(file.toPath());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    public static class LetterCountAndMapMergeTaskMock extends RecursiveTask<Map<Character, Long>> {

        private final List<String> strings;
        private final LetterCounter letterCounter;
        private final LetterCountMerger letterCountMerger;

        public LetterCountAndMapMergeTaskMock(List<String> strings, LetterCounter letterCounter, LetterCountMerger letterCountMerger) {
            this.strings = strings;
            this.letterCounter = letterCounter;
            this.letterCountMerger = letterCountMerger;
        }

        @Override
        protected Map<Character, Long> compute() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (strings.size() <= 2) {
                return strings.stream().map(letterCounter::count).reduce(letterCountMerger::merge).orElse(null);
            }

            ru.digitalhabbits.homework2.impl.LetterCountAndMapMergeTask task1 = new ru.digitalhabbits.homework2.impl.LetterCountAndMapMergeTask(strings.subList(0, strings.size() / 2), letterCounter, letterCountMerger);
            ru.digitalhabbits.homework2.impl.LetterCountAndMapMergeTask task2 = new ru.digitalhabbits.homework2.impl.LetterCountAndMapMergeTask(strings.subList(strings.size() / 2, strings.size()), letterCounter, letterCountMerger);

            task1.fork();
            task2.fork();

            return letterCountMerger.merge(task1.join(), task2.join());
        }
    }


    public static class AsyncFileLetterCounterMock implements FileLetterCounter {

        private final FileReader fileReader;
        private final LetterCounter letterCounter;
        private final LetterCountMerger letterCountMerger;

        public AsyncFileLetterCounterMock(FileReader fileReader, LetterCounter letterCounter, LetterCountMerger letterCountMerger) {
            this.fileReader = fileReader;
            this.letterCounter = letterCounter;
            this.letterCountMerger = letterCountMerger;
        }

        @Override
        public Map<Character, Long> count(File input) {
            List<String> strings = fileReader.readLines(input);
            ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            return forkJoinPool.invoke(new LetterCountAndMapMergeTaskMock(strings, letterCounter, letterCountMerger));
        }
    }

}
