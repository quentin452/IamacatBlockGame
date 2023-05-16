package fr.iamacat.iamacatblockgame;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Log4jConfiguration {

    private static final Logger logger = LogManager.getLogger(Log4jConfiguration.class);
    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE_PREFIX = "game-";
    private static final String LOG_FILE_EXTENSION = ".log";
    private static String logFileName;

    public static void configure() {
        // Create the logs directory if it doesn't exist
        File logsDir = new File(LOG_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        // Check if a log file already exists in the directory
        File[] logFiles = logsDir.listFiles((dir, name) -> name.startsWith(LOG_FILE_PREFIX) && name.endsWith(LOG_FILE_EXTENSION));
        if (logFiles != null && logFiles.length > 0) {
            // Reuse the existing log file
            logFileName = logFiles[0].getPath();
        } else {
            // Generate a unique log file name with a timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String timestamp = dateFormat.format(new Date());
            logFileName = LOG_DIRECTORY + "/" + LOG_FILE_PREFIX + timestamp + LOG_FILE_EXTENSION;
        }

    FileAppender appender = FileAppender.newBuilder()
                .withFileName(logFileName)
                .withAppend(false)
                .withLocking(false)
                .withBufferedIo(true)
                .withImmediateFlush(true)
                .setName("File")
                .setLayout(PatternLayout.newBuilder().withPattern("[%d] [%-5p] %c{1} - %m%n").build())
                .build();
        appender.start();

        logger.info("Log4j configured programmatically with log file: " + logFileName);
    }

    public static String getLogFileName() {
        return logFileName;
    }
}