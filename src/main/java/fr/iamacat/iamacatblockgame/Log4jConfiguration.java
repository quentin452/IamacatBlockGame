package fr.iamacat.iamacatblockgame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Log4jConfiguration {
    private static final Logger logger = LogManager.getLogger(Log4jConfiguration.class);

    public static void configure() {
        // Set up Log4j programmatically
        Configurator.initialize(null, "log4j2.properties");

        logger.info("Log4j configured programmatically.");
    }
}