package com.epam.brest.courses.service_rest.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtilites {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Get Date from String.
     *
     * @param dateAsString String.
     * @return Date.
     */
    public static Date getDateByString(String dateAsString) {
        String pattern = DATE_PATTERN;
        SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
        try {
            return dateformat.parse(dateAsString);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get String from Date.
     *
     * @param date Date.
     * @return String.
     */
    public static String getStringByDate(Date date) {
        String pattern = DATE_PATTERN;
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * Get month start date.
     *
     * @return Date.
     */
    public static Date getMonthStartDate() {
        Date begin;
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        begin = calendar.getTime();
        return begin;

    }

    /**
     * Get month end date.
     *
     * @return Date.
     */
    public static Date getMonthEndDate() {
        Date end;
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        end = calendar.getTime();
        return end;
    }

    /**
     * Get calendar instance.
     *
     * @return Calendar.
     */
    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    /**
     * Set calendar to beginning of the day.
     *
     * @param calendar Calendar.
     */
    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Set calendar to end of the day.
     * @param calendar Calendar.
     */
    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

}
