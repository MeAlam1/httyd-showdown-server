package com.mealam.showdown.utils.logging;

import java.util.logging.Level;

public class LogLevel {

    public static final Level INFO = new Level("INFO", Level.INFO.intValue()) {};

    public static final Level ERROR = new Level("ERROR", Level.SEVERE.intValue()) {};

    public static final Level WARNING = new Level("WARNING", Level.WARNING.intValue()) {};

    public static final Level SUCCESS = new Level("SUCCESS", Level.INFO.intValue() + 50) {};
}
