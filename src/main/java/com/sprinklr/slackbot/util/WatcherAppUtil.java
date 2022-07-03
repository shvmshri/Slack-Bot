package com.sprinklr.slackbot.util;

import com.sprinklr.slackbot.dto.SlackExternalSource;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatcherAppUtil {

    public static SlackExternalSource getFormattedObject(ArrayList<String> data, String search) {
        Pattern pattern = Pattern.compile(search);
        ArrayList<String> matchedData = new ArrayList<>();
        for (String datum : data) {
            Matcher matcher = pattern.matcher(datum);
            if (matcher.find())
                matchedData.add(datum);
        }
        SlackExternalSource slackExternalSource = new SlackExternalSource(matchedData.size());
        for (int i = 0; i < matchedData.size(); i++) {
            slackExternalSource.addOptions(i, matchedData.get(i));
        }
        return slackExternalSource;
    }

}



