package com.jobby.jobs.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Returns today's date as java.util.Date object
     */
    public static Date today() {
        return new Date();
    }

    /**
     * Returns today's date as yyyy-MM-dd format
     */
    public static String todayStr() {
        return sdf.format(new Date());
    }

    /**
     * Returns the formatted String date for the passed java.util.Date object
     */
    public static String formattedDate(Date date) {
        return date != null ? sdf.format(date) : todayStr();
    }
}
