package ru.digitalhabbits.homework2.impl;

import com.google.common.io.Files;
import ru.digitalhabbits.homework2.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class FileReaderImpl implements FileReader {

    @Override
    public Stream<String> readLines(File file) {
        try {
            return Files.readLines(file, Charset.defaultCharset()).stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
