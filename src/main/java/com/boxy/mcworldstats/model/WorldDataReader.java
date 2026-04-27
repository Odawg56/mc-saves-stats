package com.boxy.mcworldstats.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WorldDataReader {
    public static void GetDirectoryStatistics(File directory) {
        if (!directory.isDirectory()) throw new IllegalArgumentException("File must be a directory!");

        try (Stream<Path> paths = Files.walk(directory.toPath(),1)){
            paths.filter(Files::isDirectory)
                    .forEach(path -> System.out.println("Folder: " + path.toAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void GetPerSaveStats(Path saveFolder) {
        final String relativeFileName = "players/stats";
        File something = saveFolder.toFile();

    }
}
