package com.jianboke.utils;

import java.time.*;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期转换：Date —— LocalDateTime
 * Created by pengxg on 2017/4/24.
 */
public class DateTimeUtils {

    public static LocalDateTime toLdt(Date date) {
        if (date != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC);
        }
        return null;
    }

    static public Date fromLdt(LocalDateTime ldt) {
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        GregorianCalendar cal = GregorianCalendar.from(zdt);
        return cal.getTime();
    }
}
