package com.sprinklr.slackbot.service;

import com.sprinklr.slackbot.bean.Watcher;
import com.sprinklr.slackbot.factory.ChartReleaseDataFactory;
import com.sprinklr.slackbot.util.Utils;
import com.sprinklr.slackbot.util.WatcherAppMessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


@Service
public class WatcherCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherCommandService.class);
    @Autowired
    SlackMessageDispatcher slackMessageDispatcher;
    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;
    @Autowired
    private WatcherDatabaseService watcherDBService;


    public String handleWatchCommand(String commandArg, String userId, String userEmail) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();
        String time = tokens.nextToken();

        if (!chartReleaseDataFactory.validateChartRelease(chart, release)) {
            return WatcherAppMessageConstants.INVALID_ARGS_VALUE;
        }
        if (!Utils.checkTimeFormat(time)) {
            return WatcherAppMessageConstants.INVALID_TIME;
        }

        try {

            watcherDBService.addWatcherInfo(chart, release, time, userId, userEmail);
            return WatcherAppMessageConstants.SUCCESS_WATCH;

        } catch (Exception e) {

            LOGGER.error("Error occurred in database server while inserting watcher information.", e);
            return WatcherAppMessageConstants.FAILURE;

        }

    }

    public String handleUnwatchCommand(String commandArg, String userId) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();

        if (!chartReleaseDataFactory.validateChartRelease(chart, release)) {
            return WatcherAppMessageConstants.INVALID_ARGS_VALUE;
        }

        try {

            Boolean flag = watcherDBService.removeWatcherInfo(chart, release, userId);
            if (flag) {
                return WatcherAppMessageConstants.SUCCESS_UNWATCH;
            } else {
                return WatcherAppMessageConstants.NOT_A_WATCHER;
            }

        } catch (Exception e) {

            LOGGER.error("Error occurred in database server while deleting watcher information.", e);
            return WatcherAppMessageConstants.FAILURE;

        }

    }

    public String handleWatcherListCommand(String commandArg, String userId) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();

        if (!chartReleaseDataFactory.validateChartRelease(chart, release)) {
            return WatcherAppMessageConstants.INVALID_ARGS_VALUE;
        }

        try {

            List<Watcher> watcherList = watcherDBService.getWatcherUserEmails(chart, release, userId);
            ArrayList<String> userEmails = new ArrayList<String>();

            for (Watcher watcher : watcherList) {
                userEmails.add(watcher.getUserEmail());
            }

            String message = WatcherAppMessageConstants.WATCHERS_LIST;
            for (String userEmail : userEmails) {
                message = message + ", " + userEmail;
            }

            if (userEmails.isEmpty()) message = WatcherAppMessageConstants.NO_WATCHERS;
            return message;

        } catch (Exception e) {
            LOGGER.error("Error occurred in database server while fetching watcher information.", e);
            return WatcherAppMessageConstants.FAILURE;
        }

    }

}