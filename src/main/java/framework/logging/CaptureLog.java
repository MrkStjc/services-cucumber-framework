package framework.logging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import static java.util.logging.Level.FINE;

public class CaptureLog {

  private static final String LOG_FILE_NAME = "target/TestServices.log";
  private static Map<String, Logger> loggers = new HashMap<>();

  private static Level DEFAULT_FILE_LOGGING_LEVEL = FINE;
  private static Level DEFAULT_CONSOLE_LOGGING_LEVEL = FINE;


  private static FileHandler fileHandler;
  private static ConsoleHandler consoleHandler;

  public static Logger getLogger(String loggerName) {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");

    if (loggers.get(loggerName) != null) {
      return loggers.get(loggerName);
    }

    configureFileHandler();
    configureConsoleHandler();

    Logger logger = Logger.getLogger(loggerName);
    logger.setUseParentHandlers(false);

    logger.addHandler(fileHandler);
    logger.addHandler(consoleHandler);
    loggers.put(loggerName, logger);

    return logger;
  }

  public static Logger getLogger(Class clazz) {
    return getLogger(clazz.getSimpleName());
  }

  private static void configureFileHandler() {
    if (fileHandler == null) {
      try {
        fileHandler = new FileHandler(LOG_FILE_NAME, true);
      } catch (IOException | SecurityException e) {
        e.printStackTrace();
      }
      fileHandler.setFormatter(new SimpleFormatter());
    }
    fileHandler.setLevel(DEFAULT_CONSOLE_LOGGING_LEVEL);
  }

  private static void configureConsoleHandler() {
    if (consoleHandler == null) {
      consoleHandler = new ConsoleHandler();
      consoleHandler.setLevel(DEFAULT_FILE_LOGGING_LEVEL);
    }
  }

}

