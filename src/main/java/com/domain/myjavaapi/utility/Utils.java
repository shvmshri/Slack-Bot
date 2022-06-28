package com.domain.myjavaapi.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Utils {

    public static int numArgCheck(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(text);
        return tokens.countTokens();

    }

    public static Date findExpireDate(String time) {

        int timeInt = 0;
        char last = time.charAt(time.length() - 1);
        time = time.substring(0, time.length() - 1);
        if (time.length() > 0) {
            timeInt = Integer.parseInt(time);
        } else {
            return new Date();
        }

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (last == 'h' || last == 'H') {
            calendar.add(Calendar.HOUR_OF_DAY, timeInt);
        } else {
            calendar.add(Calendar.MINUTE, timeInt);
        }

        return calendar.getTime();
    }


}