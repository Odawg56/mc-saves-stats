package com.boxy.mcworldstats.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Abstract class for classes that interact with the app's AppData directory.
 */
public abstract class AppDataAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AppDataAccessor.class);
    private static final String APP_DIRECTORY = "McWorldStats";

    /**
     * Gets the relevant file from the app's AppData directory.
     * @param fileName the full name of the file to get i.e. 'userCache.json'
     * @return the File object, if successful
     */
    protected static File getAppDataFile(String fileName) {
        // Get appdata path
        String appDataPath = System.getenv("APPDATA");
        if (appDataPath == null) {
            logger.error("APPDATA environment variable not found. This code is intended for Windows.");
            throw new RuntimeException();
        }
        // Create a subdirectory if needed
        File appDir = new File(appDataPath, APP_DIRECTORY);
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                logger.warn("Failed to create directory: {}", appDir.getAbsolutePath());
            }
        }
        File targetFile = new File(appDir, fileName);
        try {
            if (!targetFile.exists()) {
                if (!targetFile.createNewFile()) {
                    logger.warn("Failed to create file: {}",targetFile.getAbsolutePath());
                } else {
                    logger.info("Target file not found, created: {}", targetFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            logger.error("An error occurred while creating the file: {}", e.getMessage());
        }

        return targetFile;
    }
}
