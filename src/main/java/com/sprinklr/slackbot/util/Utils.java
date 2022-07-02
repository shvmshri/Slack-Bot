package com.sprinklr.slackbot.util;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Utils {

    public static int numArgs(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(text);
        return tokens.countTokens();

    }

    public static boolean checkTimeFormat(String duration) {

        if ((duration.length() - 1) > 0) {
            try {
                int timeInt = Integer.parseInt(duration.substring(0, duration.length() - 1));
                char last = duration.charAt(duration.length() - 1);
                return (last == 'H' || last == 'h' || last == 'M' || last == 'm');
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }

    public static Date findExpireDate(String duration) {

        if (!checkTimeFormat(duration)) {
            return new Date();
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        char lastChar = duration.charAt(duration.length() - 1);
        int timeInt = Integer.parseInt(duration.substring(0, duration.length() - 1));
        if (lastChar == 'h' || lastChar == 'H') {
            calendar.add(Calendar.HOUR_OF_DAY, timeInt);
        } else {
            calendar.add(Calendar.MINUTE, timeInt);
        }

        return calendar.getTime();
    }

}