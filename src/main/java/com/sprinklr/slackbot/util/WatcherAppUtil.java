package com.sprinklr.slackbot.util;

import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.sprinklr.slackbot.dto.SlackExternalSource;
import com.sprinklr.slackbot.dto.SlackOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatcherAppUtil {

    private static final Map<String, String> repoProjectIdMap = new HashMap<>();
    private static final String VALUE = "value-0";
    private static final String DEFAULT_REPO_NAME = "Sprinklr Main App";

    static {
        repoProjectIdMap.put("Sprinklr Main App", "");
    }

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

    public static List<OptionObject> getRepoNamesObjectList() {
        SlackOptions slackOptions = new SlackOptions(repoProjectIdMap.size());
        int i = 0;
        for (String repo : repoProjectIdMap.keySet()) {
            slackOptions.addOptions(i, repo);
            i++;
        }
        return slackOptions.getOptions();
    }

    public static OptionObject getDefaultValueObject() {
        OptionObject optionObject = new OptionObject();
        optionObject.setValue(VALUE);
        optionObject.setText(new PlainTextObject(DEFAULT_REPO_NAME, true));
        return optionObject;
    }

}



