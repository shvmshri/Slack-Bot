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

//    public static boolean checkTimeFormat(String duration) {
//
//        if ((duration.length() - 1) > 0) {
//            try {
//                double timeDouble = Double.parseDouble(duration.substring(0, duration.length() - 1));
//                char last = duration.charAt(duration.length() - 1);
//                return (last == 'H' || last == 'h' || last == 'M' || last == 'm');
//            } catch (Exception e) {
//                return false;
//            }
//        } else {
//            return false;
//        }
//
//    }

    public static boolean checkTimeFormat(String duration) {
        if (duration == null || duration.isEmpty()) {
            return false;
        }
        try {
            double timeDouble = Double.parseDouble(duration);
            return true;
        } catch (Exception e) {
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

        double timeDouble = Double.parseDouble(duration);
        calendar.add(Calendar.MINUTE, (int) Math.round(timeDouble * 60));
        return calendar.getTime();
    }

//    public static Date findExpireDate(String duration) {
//
//        if (!checkTimeFormat(duration)) {
//            return new Date();
//        }
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        char lastChar = duration.charAt(duration.length() - 1);
//        double timeDouble = Double.parseDouble(duration.substring(0, duration.length() - 1));
//        if (lastChar == 'h' || lastChar == 'H') {
//            calendar.add(Calendar.MINUTE, (int) Math.round(timeDouble * 60));
//        } else {
//            calendar.add(Calendar.SECOND, (int) Math.round(timeDouble * 60));
//        }
//
//        return calendar.getTime();
//    }

}