package com.boxy.mcworldstats.model;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WorldDataReader {
    public static void GetDirectoryStatistics(File directory) {
        if (!directory.isDirectory()) throw new IllegalArgumentException("File must be a directory!");

        double directoryHrs = 0.0;

        try (Stream<Path> paths = Files.walk(directory.toPath(),1)){
            Path[] saves = paths.filter(Files::isDirectory).toArray(Path[]::new);
            for(Path save : saves) {
                directoryHrs += GetPerSaveStats(save);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Total Hrs for given directory: "+directoryHrs);
    }

    private static double GetPerSaveStats(Path saveFolder) {
        Path statsFolder = saveFolder.resolve("players/stats").normalize();
        File fileEq = statsFolder.toFile();

        double worldHrs = 0.0;

        // only get stats for stats directories that actually exist
        if (fileEq.isDirectory() && fileEq.exists()) {
//            String longname = statsFolder.toAbsolutePath().toString();
//            System.out.println(longname);

            try (Stream<Path> individual_stat = Files.walk(statsFolder,1)) {
               Path[] statFiles = individual_stat
                       .filter(Files::isRegularFile)
                       .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                       .toArray(Path[]::new);
               for (Path stat : statFiles) {
                   worldHrs += GetPlayerHours(stat);
               }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return worldHrs;
    }

    private static double GetPlayerHours(Path statFile) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(statFile);
        int time_played_ticks = root.get("stats").get("minecraft:custom").get("minecraft:play_time").asInt(0);

        return (double)time_played_ticks/72000;
    }
}
