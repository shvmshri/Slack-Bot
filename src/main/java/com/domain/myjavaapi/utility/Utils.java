package com.domain.myjavaapi.utility;

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

    public static boolean checkTimeFormat(String time){

        if ((time.length()-1) > 0) {
            try{
                int timeInt =   Integer.parseInt(time.substring(0, time.length() - 1));
                char last = time.charAt(time.length() - 1);
                return (last == 'H' || last == 'h' || last == 'M' || last == 'm');
            } catch(Exception e){
                return false;
            }
        } else {
           return false;
        }

    }

    public static Date findExpireDate(String time) {

        if(!checkTimeFormat(time)){
            return new Date();
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        char lastChar = time.charAt(time.length()-1);
        int timeInt = Integer.parseInt(time.substring(0,time.length()-1));
        if (lastChar == 'h' || lastChar == 'H') {
            calendar.add(Calendar.HOUR_OF_DAY, timeInt);
        } else {
            calendar.add(Calendar.MINUTE, timeInt);
        }

        return calendar.getTime();
    }

}