package ru.digitalhabbits.homework2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.digitalhabbits.homework2.impl.FileReaderImpl;

import java.io.File;
import java.util.List;

import static com.google.common.io.Resources.getResource;
import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    private static FileReader fileReader;

    @BeforeAll
    static void init() {
        fileReader = new FileReaderImpl();
    }


    @Test
    void read_lines() {
        List<String> strings = fileReader.readLines(getFile("test.txt"));
        assertEquals(strings.size(), 1000);
    }

    @Test
    void read_empty_file() {
        List<String> strings = fileReader.readLines(getFile("empty.txt"));
        assertEquals(strings.size(), 0);
    }

    @Test
    void read_not_exist_file() {
        Throwable throwable = assertThrows(IllegalArgumentException.class, ()-> fileReader.readLines(getFile("test1.txt")));
        assertNotNull(throwable.getMessage());
    }

    private File getFile(String name) {
        return new File(getResource(name).getPath());
    }
}