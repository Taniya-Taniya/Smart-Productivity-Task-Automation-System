package utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidator {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s, dateFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalTime parseTime(String s) {
        try {
            return LocalTime.parse(s, timeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean isValidInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidIntegerInRange(String s, int min, int max) {
        try {
            int v = Integer.parseInt(s);
            return v >= min && v <= max;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidPositiveInt(String s) {
        try {
            int v = Integer.parseInt(s);
            return v > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
