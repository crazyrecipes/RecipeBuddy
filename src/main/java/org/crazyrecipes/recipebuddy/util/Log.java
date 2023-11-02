package org.crazyrecipes.recipebuddy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log provides simple functionality for logging events by class.
 * Log events can have one of three severity levels:
 *  - 0: [ INFO ]
 *  - 1: [ WARN ]
 *  - 2: [ ERROR ]
 */
public class Log {
    private static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_INFO = " [ INFO ] ";
    private static final String LOG_WARN = " [ WARN ] ";
    private static final String LOG_ERROR = " [ ERROR ] ";
    private final String className;

    /**
     * Instantiates a Log for this class
     * @param className This class's name
     */
    public Log(String className) {
        this.className = className;
    }

    /**
     * Logs an event with a specified severity
     * @param level Severity
     * @param entry Event to log
     */
    public void print(int level, String entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(LOG_DATE_FORMAT.format(new Date()));
        switch(level) {
            case 1:
                sb.append(LOG_WARN);
                break;
            case 2:
                sb.append(LOG_ERROR);
                break;
            default:
                sb.append(LOG_INFO);
                break;
        }
        sb.append(className);
        sb.append(" ".repeat(Math.max(0, 24 - className.length())));
        sb.append(": ");
        sb.append(entry);
        System.out.println(sb);
    }

    /**
     * Logs an event with severity INFO.
     * @param entry Event to log
     */
    public void print(String entry) { this.print(0, entry); }
}
