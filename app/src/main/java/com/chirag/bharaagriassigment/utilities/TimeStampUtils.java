package com.chirag.bharaagriassigment.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeStampUtils {
    private static TimeZone tz = TimeZone.getDefault();

    public static long dateToSeconds(String givenDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date mDate = sdf.parse(givenDateString);
            if (mDate != null) {
                return TimeUnit.MILLISECONDS.toSeconds(mDate.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getPrettyTime(long seconds) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeZone(tz);
        calendar.setTimeInMillis(seconds * 1000L);
        Calendar now = Calendar.getInstance(Locale.ENGLISH);
        now.setTimeZone(tz);
        return new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(calendar.getTime());
    }
}
