package com.boxy.mcworldstats.model;

import com.boxy.mcworldstats.util.AppDataAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesManager extends AppDataAccessor {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);
    private static final File propertiesFile = getAppDataFile("cfg.properties");

    // might want to implement some logic such that the system only pulls/pushes when necessary
    // and abstract that functionality away from callers.
    // ^^ this will likely include keeping track of if the properties have changed or not

    private static void pull() {
        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(propertiesFile);
            prop.load(fis);
            fis.close();

            // set static fields such that they can be changed by getters and setters.
        } catch (Exception e) {
            logger.error("Failed to load properties from file", e);
        }
    }
    private static void push() {
        try {
            // this will be pull in reverse

            // create new properties and store fields in there

            // write to file

            // close file stream

        } catch (Exception e) {
            logger.error("Failed to load properties from file", e);
        }
    }

}
