package com.sprinklr.slackbot.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sprinklr.slackbot.dto.SlackExternalSource;
import com.sprinklr.slackbot.factory.ChartReleaseDataFactory;
import com.sprinklr.slackbot.util.WatcherAppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Service
public class SlackWatcherAppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackWatcherAppService.class);
    private static final Gson gson = new Gson();
    private static final String CHART_NAME = "chartName";
    private static final String K8S = "k8s";
    private static final String WATCH_CHART = "WATCH_CHART";
    private static final String UNWATCH_CHART = "UNWATCH_CHART";
    private static final String WATCHERS_LIST_CHART = "WATCHERS_LIST_CHART";

    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;

    public SlackExternalSource getSlackObject(String actionId, String search, String privateMetadata) {
        LOGGER.error("{SlackDeployerAppService_getSlackObject} actionId: " + actionId + " search: " + search +
                " privateMetadata: " + privateMetadata);

        Type privateMetadataMapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> metadata = gson.fromJson(privateMetadata, privateMetadataMapType);
        String chartName = metadata.get(CHART_NAME);
        //String result = chartReleaseDataFactory.fetchChartReleaseData();
        //For testing only
        String result = "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}";
        Type chartReleaseMapType = new TypeToken<Map<String, Map<String, ArrayList<String>>>>() {
        }.getType();
        Map<String, Map<String, ArrayList<String>>> chartReleaseMap = gson.fromJson(result, chartReleaseMapType);

        if (actionId.equals(WATCH_CHART) || actionId.equals(UNWATCH_CHART) || actionId.equals(WATCHERS_LIST_CHART)) {
            return getChartSlackObject(search, chartReleaseMap);
        } else {
            return getReleaseSlackObject(search, chartName, chartReleaseMap);
        }

    }

    //  <---------------------------------- PRIVATE METHODS ---------------------------------->

    private SlackExternalSource getChartSlackObject(String search, Map<String, Map<String, ArrayList<String>>> chartReleaseMap) {
        LOGGER.error("{SlackDeployerAppService_getChartSlackObject} search: " + search + " chartReleaseMap: " + chartReleaseMap);
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> chartNames = gson.fromJson(chartReleaseMap.keySet().toString(), listType);
        Collections.sort(chartNames);
        return WatcherAppUtil.getFormattedObject(chartNames, search);
    }

    private SlackExternalSource getReleaseSlackObject(String search, String chartName, Map<String, Map<String, ArrayList<String>>> chartReleaseMap) {
        LOGGER.error("{SlackDeployerAppService_getReleaseSlackObject} search: " + search + " chartName: " + chartName +
                " chartReleaseMap: " + chartReleaseMap);
        ArrayList<String> releaseNames = chartReleaseMap.get(chartName).get(K8S);
        Collections.sort(releaseNames);
        return WatcherAppUtil.getFormattedObject(releaseNames, search);
    }

}