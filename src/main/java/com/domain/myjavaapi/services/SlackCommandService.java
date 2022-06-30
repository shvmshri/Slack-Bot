package com.domain.myjavaapi.services;

import com.domain.myjavaapi.models.Watcher;
import com.domain.myjavaapi.objectFactory.ChartReleaseDataFactory;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import com.domain.myjavaapi.utility.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


@Service
public class SlackCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackCommandService.class);
    @Autowired
    SlackMessageDispatchingService slackMessageDispatchingService;
    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;
    @Autowired
    private WatcherDatabaseService watcherDBService;

    private boolean chartReleaseNamesValidate(String chart, String release, String userId) {
        if (!chartReleaseDataFactory.validateChartRelease(chart, release)) {
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.INVALID_ARGS_VALUE);
            return false;
        }
        return true;
    }

    private boolean timeFormatValidate(String time, String userId) {
        if (!Utils.checkTimeFormat(time)) {
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.INVALID_TIME);
            return false;
        }
        return true;
    }

    public void handleWatchCommand(String commandArg, String userId, String userEmail) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();
        String time = tokens.nextToken();

        if ((!chartReleaseNamesValidate(chart, release, userId)) || (!timeFormatValidate(time, userId))) {
            return;
        }

        try {
            watcherDBService.addWatcherInfo(chart, release, time, userId, userEmail);
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.SUCCESS_WATCH);
        } catch (Exception e) {
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.FAILURE);
            LOGGER.error("Error occurred in database server while inserting watcher information.", e);
        }

    }

    public void handleUnwatchCommand(String commandArg, String userId) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();

        if (chartReleaseNamesValidate(chart, release, userId)) {
            return;
        }

        try {
            Boolean flag = watcherDBService.removeWatcherInfo(chart, release, userId);
            if (flag) {
                slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.SUCCESS_UNWATCH);
            } else {
                slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.NOT_A_WATCHER);
            }
        } catch (Exception e) {
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.FAILURE);
            LOGGER.error("Error occurred in database server while deleting watcher information.", e);
        }

    }

    public void handleWatcherListCommand(String commandArg, String userId) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();

        if (chartReleaseNamesValidate(chart, release, userId)) {
            return;
        }

        try {
            List<Watcher> watcherList = watcherDBService.getWatcherUserEmails(chart, release, userId);
            ArrayList<String> userEmails = new ArrayList<String>();

            for (Watcher watcher : watcherList) {
                userEmails.add(watcher.getUserEmail());
            }

            String message = SlackMessageConstants.WATCHERS_LIST;
            for (String userEmail : userEmails) {
                message = message + ", " + userEmail;
            }
            if (userEmails.isEmpty()) message = SlackMessageConstants.NO_WATCHERS;
            slackMessageDispatchingService.slackSendMsg(userId, message);
        } catch (Exception e) {
            slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.FAILURE);
            LOGGER.error("Error occurred in database server while fetching watcher information.", e);
        }

    }

}