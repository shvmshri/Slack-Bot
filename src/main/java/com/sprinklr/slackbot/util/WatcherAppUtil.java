package com.sprinklr.slackbot.util;

import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.sprinklr.slackbot.dto.SlackExternalSource;
import com.sprinklr.slackbot.dto.SlackOptions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatcherAppUtil {

    private static final ArrayList<String> repoList = new ArrayList<>();
    private static final String VALUE = "value-0";
    private static final String DEFAULT_REPO_NAME = "Sprinklr Main App";

    static {
        repoList.add("Sprinklr Main App");
       // repoList.add("Spinklr custom helloo");
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
        SlackOptions slackOptions = new SlackOptions(repoList.size());
        int i = 0;
        for (String repo : repoList) {
            slackOptions.addOptions(i, repo);
            i++;
        }
        System.out.println(slackOptions.getOptions().toString());
        return slackOptions.getOptions();
    }

    public static OptionObject getDefaultValueObject() {
        OptionObject optionObject = new OptionObject();
        optionObject.setValue(VALUE);
        optionObject.setText(new PlainTextObject(DEFAULT_REPO_NAME, true));
        return optionObject;
    }

}