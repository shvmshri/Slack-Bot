package com.sprinklr.slackbot.service;


import com.google.gson.reflect.TypeToken;
import com.sprinklr.slackbot.dto.SlackExternalSource;
import com.sprinklr.slackbot.enums.WatcherAppSlackCommand;
import com.sprinklr.slackbot.factory.ChartReleaseDataFactory;
import com.sprinklr.slackbot.util.Utils;
import com.sprinklr.slackbot.util.WatcherAppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class SlackWatcherAppService {
    private static final String CHART_NAME = "chartName";
    private static final String REPO_NAME = "repoName";
    private static final String CHART_ACTION_ID = "chartActionId";

    private static final List<String> repositryNames = new ArrayList<>(Arrays.asList("Sprinklr Main App"));

    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;

    public SlackExternalSource getSlackObject(String actionId, String search, String privateMetadata) {
        Type privateMetadataMapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> metadata = Utils.fromJson(privateMetadata, privateMetadataMapType);

        String chartName = metadata.get(CHART_NAME);
        String repo = metadata.get(REPO_NAME);

        for (WatcherAppSlackCommand watcherAppSlackCommand : WatcherAppSlackCommand.values()) {
            if (actionId.equals(watcherAppSlackCommand.getChartActionId())) {
                return getChartSlackObject(search, repo);
            }
        }

        return getReleaseSlackObject(search, chartName, repo);
    }

    //  <---------------------------------- PRIVATE METHODS ---------------------------------->

    private SlackExternalSource getChartSlackObject(String search, String repo) {
        ArrayList<String> chartNames = chartReleaseDataFactory.getChartNames(repo);

        Collections.sort(chartNames);
        return WatcherAppUtil.getFormattedObject(chartNames, search);
    }

    private SlackExternalSource getReleaseSlackObject(String search, String chartName, String repo) {
        ArrayList<String> releaseNames = chartReleaseDataFactory.getReleaseNames(repo, chartName);

        Collections.sort(releaseNames);
        return WatcherAppUtil.getFormattedObject(releaseNames, search);
    }

}