package com.boxy.mcworldstats.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class WorldDataReader {

    public static final Logger logger = LoggerFactory.getLogger(WorldDataReader.class);

    public static void GetDirectoryStatistics(File directory) {
        if (!directory.isDirectory()) throw new IllegalArgumentException("File must be a directory!");

        double directoryHrs = 0.0;

        try (Stream<Path> paths = Files.walk(directory.toPath(),1)){
            Path[] saves = paths.filter(Files::isDirectory).toArray(Path[]::new);
            Path[] saves_without_root = Arrays.copyOfRange(saves,1,saves.length);

            logger.info("Found {} save files in {}", saves_without_root.length, directory.getName());
            for(Path savePath : saves_without_root) {
                directoryHrs += GetPerSaveHours(savePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Total Hrs for given directory: {}",directoryHrs);
    }

    private static double GetPerSaveHours(Path saveFolder) {
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
                logger.info("Found {} stats files in save {}", statFiles.length, saveFolder.getFileName());
                for (Path stat : statFiles) {
                    worldHrs += GetPlayerHours(stat);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return worldHrs;
    }

    /**
     * Returns the hours played for a given player statistics file, and updates the session memory with other desired statistics.
     * @param statFile the path to a player statistics .JSON file. filename must be UUID.
     * @return the number of hours played on the given stats file.
     */
    private static double GetPlayerHours(Path statFile) {
        String uuid = TrimFileExtension(statFile);
        logger.debug("Reading stats for UUID: {}", uuid);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(statFile);
        int time_played_ticks = root.get("stats").get("minecraft:custom").get("minecraft:play_time").asInt(0);
        double time_played_hours = (double)time_played_ticks/72000;

        Player p = SessionMemory.getPlayer(uuid);
        p.incrementTotalHrs(time_played_hours);
        p.ensureDisplayName();
        SessionMemory.setPlayer(p);

        return time_played_hours;
    }

    /**
     * Gets just the filename of a file at a given path, removing the file extension
     * e.g. 'temp\test.txt' becomes 'test'.
     * Precondition: {@code fileToTrim} exists and isn't a directory.
     * @param fileToTrim the path at which a file to trim is located.
     * @return the filename without extension.
     */
    private static String TrimFileExtension(Path fileToTrim) {
        String fileName = fileToTrim.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0) {
            return fileName;
        }
        return fileName.substring(0,dotIndex);
    }
}
