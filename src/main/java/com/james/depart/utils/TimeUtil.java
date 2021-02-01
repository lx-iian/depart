package com.james.depart.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    public static int getFullYear() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        return year;
    }

    public static int getMonth() {
        LocalDate now = LocalDate.now();
        int monthValue = now.getMonthValue();
        return monthValue;
    }

    public static int getDay() {
        LocalDate now = LocalDate.now();
        int dayOfMonth = now.getDayOfMonth();
        return dayOfMonth;
    }

    public static String getFullYearAndMonth() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    public static String getFullDate() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    public static String getFullYearAndFullMonth() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        return year + "-" + monthValue;
    }

    public static String getStartDate(String startDate) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-M");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        return LocalDateTime.parse(startDate, inputFormat).format(outputFormat);
    }
}
