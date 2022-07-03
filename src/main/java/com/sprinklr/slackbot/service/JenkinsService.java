package com.sprinklr.slackbot.service;

import com.sprinklr.slackbot.bean.JenkinsJobInfo;
import com.sprinklr.slackbot.util.SlackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JenkinsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);
    private static final String DEFAULT_TIME = "5h";
    @Autowired
    WatcherDatabaseService watcherDBService;
    @Autowired
    SlackMessageDispatcher slackMessageDispatcher;

    private void handleNotifyUsers(String chart, String release, String userId, String jobLink) {

        String message = "A build is invoked by <@" + userId + "> on \nChart `" + chart + "`\nRelease `" + release + "`\nJob `" + jobLink + "`";

        try {
            List<String> userIDList;
            if (release.equals("all")) {
                userIDList = watcherDBService.getWatcherUserIds(chart);
            } else {
                userIDList = watcherDBService.getWatcherUserIds(chart, release);
            }

            try {
                userIDList.remove(userId);
            } catch (Exception e) {
                LOGGER.error("Error occurred while finding Slack userID of the jenkins builder", e);
            }

            slackMessageDispatcher.sendMessage(userIDList, message);
        } catch (Exception e) {
            LOGGER.error("Error occurred in database server while fetching watcher information to send notification about the build", e);
        }

    }

    public void handleJobTrigger(JenkinsJobInfo job) {
        String chart = job.getChartName();
        String release = job.getReleaseName();
        String userEmail = job.getUserEmail();
        String jobLink = job.getJobLink();

        String userId = SlackUtil.findSlackUserId(userEmail);
        handleNotifyUsers(chart, release, userId, jobLink);


        //To add Jenkins user as a watcher for default time

//        if (userId == null) {
//            LOGGER.error("[JenkinsService_CRITICAL] The user triggering build is not present in Slack Workspace. Hence, can't add the Jenkins User as a Watcher");
//            return;
//        }
//
//        try {
//            if (!watcherDBService.searchAWatcher(chart, release, userId)) {
//                try {
//                    watcherDBService.addWatcherInfo(chart, release, DEFAULT_TIME, userId);
//                } catch (Exception e) {
//                    LOGGER.error("Error occurred in database server while inserting Jenkins build invoker as a Watcher", e);
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error("Error occurred while searching Jenkins build invoker in database server ", ex);
//        }

    }

}