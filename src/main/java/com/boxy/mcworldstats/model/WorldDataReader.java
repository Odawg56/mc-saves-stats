package com.boxy.mcworldstats.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WorldDataReader {

    public static Logger logger = LoggerFactory.getLogger(WorldDataReader.class);

    public static void GetDirectoryStatistics(File directory) {
        if (!directory.isDirectory()) throw new IllegalArgumentException("File must be a directory!");

        double directoryHrs = 0.0;

        try (Stream<Path> paths = Files.walk(directory.toPath(),1)){
            Path[] saves = paths.filter(Files::isDirectory).toArray(Path[]::new);
            logger.info("Found {} save files", saves.length);
            for(Path save : saves) {
                directoryHrs += GetPerSaveStats(save);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Total Hrs for given directory: {}",directoryHrs);
    }

    private static double GetPerSaveStats(Path saveFolder) {
        Path statsFolderOld = saveFolder.resolve("players/stats").normalize();
        Path statsFolderNew = saveFolder.resolve("stats").normalize();
        Path usableStatsFolder = null;
        boolean statsFolderFound = false;

        double worldHrs = 0.0;

        // Choose correct stats directory regardless of old/new format.
        if (statsFolderOld.toFile().isDirectory() && statsFolderOld.toFile().exists()) {
            statsFolderFound = true;
            usableStatsFolder = statsFolderOld;
        } else if (statsFolderNew.toFile().isDirectory() && statsFolderNew.toFile().exists()) {
            statsFolderFound = true;
            usableStatsFolder = statsFolderNew;
        } else {
            logger.warn("Could not find valid stats folder for save '{}'",saveFolder.getFileName());
        }

        if (statsFolderFound) {
            try (Stream<Path> individual_stat = Files.walk(usableStatsFolder,1)) {
                Path[] statFiles = individual_stat
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                        .toArray(Path[]::new);
                logger.info("Found {} stats files in directory {}", statFiles.length, usableStatsFolder.toAbsolutePath());
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
        logger.info("Reading stats for UUID: {}", statFile.getFileName());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(statFile);
        int time_played_ticks = root.get("stats").get("minecraft:custom").get("minecraft:play_time").asInt(0);

        return (double)time_played_ticks/72000;
    }
}
