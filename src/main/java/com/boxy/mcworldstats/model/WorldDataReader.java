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
                    .forEach(WorldDataReader::GetPerSaveStats);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void GetPerSaveStats(Path saveFolder) {
        Path statsFolder = saveFolder.resolve("players/stats").normalize();
        String longname = statsFolder.toAbsolutePath().toString();
        System.out.println(longname);
    }
}
