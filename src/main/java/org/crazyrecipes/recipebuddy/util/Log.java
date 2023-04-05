package org.crazyrecipes.recipebuddy.util;

import org.crazyrecipes.recipebuddy.RecipeBuddyMap;

import java.util.Date;

public class Log {
    private final String LOG_OK = " [  OK  ] ";
    private final String LOG_WARN = " [ WARN ] ";
    private final String LOG_ERROR = " [ERROR!] ";

    private String className;
    private final int LOG_LEVEL = RecipeBuddyMap.LOG_LEVEL;

    public Log(String className) {
        this.className = className;
    }

    public void print(String entry) {
        if(LOG_LEVEL < 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("--- ");
            sb.append(new Date());
            sb.append(LOG_OK);
            sb.append(className);
            sb.append(": ");
            sb.append(entry);
            System.out.println(sb.toString());
        }
    }

    public void print(int level, String entry) {
        if(level >= LOG_LEVEL) {
            StringBuilder sb = new StringBuilder();
            sb.append("--- ");
            sb.append(new Date());
            switch(level) {
                case 1:
                    sb.append(LOG_WARN);
                    break;
                case 2:
                    sb.append(LOG_ERROR);
                    break;
                default:
                    sb.append(LOG_OK);
                    break;
            }
            sb.append(className);
            sb.append(": ");
            sb.append(entry);
            System.out.println(sb.toString());
        }
    }
}