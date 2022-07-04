package com.sprinklr.slackbot.service;

import com.sprinklr.slackbot.factory.ChartReleaseDataFactory;
import com.sprinklr.slackbot.util.Utils;
import com.sprinklr.slackbot.util.WatcherAppMessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WatcherCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherCommandService.class);

    @Autowired
    SlackMessageDispatcher slackMessageDispatcher;
    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;
    @Autowired
    private WatcherDatabaseService watcherDBService;


    public String handleWatchCommand(String chart, String release, String duration, String userId) {

        if (!Utils.checkTimeFormat(duration)) {
            return WatcherAppMessageConstants.INVALID_TIME;
        }

        try {

            watcherDBService.addWatcherInfo(chart, release, duration, userId);
            return WatcherAppMessageConstants.SUCCESS_WATCH + "\nChart `" + chart + "`\nRelease `" + release + "`\n";

        } catch (Exception e) {

            LOGGER.error("Error occurred in database server while inserting watcher information.", e);
            return WatcherAppMessageConstants.FAILURE + "\nChart `" + chart + "`\nRelease `" + release + "`\n";

        }

    }

    public String handleUnwatchCommand(String chart, String release, String userId) {

        try {

            Boolean flag = watcherDBService.removeWatcherInfo(chart, release, userId);
            if (flag) {
                return WatcherAppMessageConstants.SUCCESS_UNWATCH + "\nChart `" + chart + "`\nRelease `" + release + "`\n";
            } else {
                return WatcherAppMessageConstants.NOT_A_WATCHER + "\nChart `" + chart + "`\nRelease `" + release + "`\n";
            }

        } catch (Exception e) {

            LOGGER.error("Error occurred in database server while deleting watcher information.", e);
            return WatcherAppMessageConstants.FAILURE + "\nChart `" + chart + "`\nRelease `" + release + "`\n";

        }

    }

    public String handleWatcherListCommand(String chart, String release) {

        try {

            List<String> userIds = watcherDBService.getWatcherUserIds(chart, release);

            String message = WatcherAppMessageConstants.WATCHERS_LIST + "Chart `" + chart + "`\nRelease `" + release + "`\nWatchers";
            for (String userId : userIds) {
                message = message + " <@" + userId + ">";
            }

            if (userIds.isEmpty()) {
                message = WatcherAppMessageConstants.NO_WATCHERS;
            }
            return message;

        } catch (Exception e) {
            LOGGER.error("Error occurred in database server while fetching watcher's information.", e);
            return WatcherAppMessageConstants.FAILURE;
        }

    }

}