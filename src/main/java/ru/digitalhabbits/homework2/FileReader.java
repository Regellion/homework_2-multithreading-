package ru.digitalhabbits.homework2;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

/**
 * Sequential file reader
 */
public interface FileReader {

    List<String> readLines(File file);

}
