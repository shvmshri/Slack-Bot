package com.sprinklr.slackbot.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;


public class Utils {

    private static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

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

}