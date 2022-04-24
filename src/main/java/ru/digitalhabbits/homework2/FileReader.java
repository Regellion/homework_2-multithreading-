package ru.digitalhabbits.homework2;

import java.io.File;
import java.util.List;

/**
 * Sequential file reader
 */
public interface FileReader {

    List<String> readLines(File file);

}
