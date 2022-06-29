package com.domain.myjavaapi.services;

import com.domain.myjavaapi.models.Watcher;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class SlackCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackCommandService.class);
    @Autowired
    private WatcherDatabaseService watcherDBService;
    @Autowired
    SlackMessageDispatchingService slackMessageDispatchingService;

    public void handleWatchCommand(String commandArg, String userId, String userEmail) {

        StringTokenizer tokens = new StringTokenizer(commandArg);
        String chart = tokens.nextToken();
        String release = tokens.nextToken();
        String time = tokens.nextToken();

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

        try {
            List<Watcher> watcherList = watcherDBService.getWatchersList(chart, release, userId);
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


        //add command handler
    }

//    public void handleNotifyUsers(JenkinsJobInfo job) {
//
//        String chart = job.getChartName();
//        String release = job.getReleaseName();
//        String userStartingBuild = job.getUserEmail();
//        String message = "A build is invoked by a user with email_Id : " + userStartingBuild + " on chartName = " + chart + " and releaseName = " + release;
//
//        try {
//            List<String> userIDList = watcherDBService.searchWatcherUserIds(job);
//            slackMessageDispatchingService.slackSendMsg(userIDList, message);
//        } catch (Exception e) {
//            LOGGER.error("Error occurred in database server while fetching watcher information to send notification about the build", e);
//        }
//
//    }

}