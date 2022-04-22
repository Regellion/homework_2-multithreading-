package ru.digitalhabbits.homework2;

import static com.google.common.io.Resources.getResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import ru.digitalhabbits.homework2.impl.AsyncFileLetterCounter;
import ru.digitalhabbits.homework2.impl.FileReaderImpl;
import ru.digitalhabbits.homework2.impl.LetterCountMergerImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class E2ETests {
    private static final FileReader fileReader = new FileReaderImpl();
    private static final LetterCountMerger letterCountMerger = new LetterCountMergerImpl();

    @Test
    void async_file_letter_counting_should_return_predicted_count() {
        var file = getFile("test.txt");
        var counter = new AsyncFileLetterCounter();

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
    void emptyFileTest() {
        var file = getFile("empty.txt");

        Map<Character, Long> count = new AsyncFileLetterCounter().count(file);
        assertThat(count).isNull();
    }


    @Test
    void fileReaderInMainThread() {
        var file = getFile("test.txt");
        FileReader fileReader = (f) -> {
            try {
                Thread.sleep(1000);
                return Files.readLines(f, Charset.defaultCharset()).stream();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return Stream.empty();
        };

        long start = System.currentTimeMillis();
        fileReader.readLines(file);
        long end = System.currentTimeMillis();

        assertThat(end - start).isGreaterThan(1000);

    }

    @Test
    void asyncLetterCounterInParallelThreads() {
        var file = getFile("test2.txt");

        LetterCounter letterCounter = (line) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<Character, Long> charMap = new HashMap<>();
            line.chars()
                    .forEach(c -> {
                        charMap.computeIfAbsent((char) c, (k) -> 0L);
                        charMap.computeIfPresent((char) c, (k, v) -> v + 1);
                    });
            return charMap;
        };

        FileLetterCounter fileLetterCounter = (f) -> fileReader
                .readLines(f)
                .parallel()
                .map(letterCounter::count)
                .reduce(letterCountMerger::merge)
                .orElse(null);


        long start = System.currentTimeMillis();
        Map<Character, Long> count = fileLetterCounter.count(file);
        long end = System.currentTimeMillis();

        assertThat(end - start).isLessThan(10000);
        assertThat(count).containsOnly(
                entry('a', 3L),
                entry('b', 2L),
                entry('c', 2L),
                entry('d', 2L),
                entry('e', 1L)
        );
    }

    private File getFile(String name) {
        return new File(getResource(name).getPath());
    }
}
