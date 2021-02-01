package com.james.depart.utils;

import ch.qos.logback.core.util.DatePatternToRegexUtil;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.unit.DataUnit;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DateTest {

    @Test
    public void getYearAndMonth() {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        System.out.println(year);
        System.out.println(monthValue);
    }

    @Test
    public void getYearAndMonthF() {

        LocalDate now = LocalDate.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        System.out.println(now);
        System.out.println(format);
    }

    @Test
    public void getYear() {
        Year now = Year.now();
        MonthDay monthDay = MonthDay.now();
        System.out.println(now);
        System.out.println(monthDay);

    }

    @Test
    public void getA() {
        String fullYearAndFullMonth = TimeUtil.getFullYearAndFullMonth();
        String startDate = TimeUtil.getStartDate(fullYearAndFullMonth);
        System.out.println(startDate);
    }

    @Test
    public void getDate() {
        String fullDate = TimeUtil.getFullDate();
        System.out.println(fullDate);
    }
    @Test
    public void getUUID() {
        long abs = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        System.out.println(abs);
    }
}
