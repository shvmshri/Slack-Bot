package com.domain.myjavaapi.services;

import com.domain.myjavaapi.models.JenkinsJobInfo;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import com.domain.myjavaapi.utility.SlackUtil;
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
    SlackMessageDispatchingService slackMessageDispatchingService;

    private void handleNotifyUsers(String chart, String release, String userEmail, String jobLink) {

        String message = "A build is invoked by a user with email_Id : " + userEmail + " on chartName = " + chart + " and releaseName = " + release + "and the job link is: " + jobLink;

        try {
            List<String> userIDList = watcherDBService.getWatcherUserIds(chart, release);
            slackMessageDispatchingService.slackSendMsg(userIDList, message);
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

        handleNotifyUsers(chart, release, userEmail, jobLink);
        if (userId == null) {
            LOGGER.error("[JenkinsService_CRITICAL] The user triggering build is not present in Slack Workspace. Hence, can't add the Jenkins User as a Watcher");
            return;
        }

        try {
            if (!watcherDBService.searchAWatcher(chart, release, userId)) {
                try {
                    watcherDBService.addWatcherInfo(chart, release, DEFAULT_TIME, userId, userEmail);
                    slackMessageDispatchingService.slackSendMsg(userId, SlackMessageConstants.SUCCESS_JENKINS_AS_WATCHER);
                } catch (Exception e) {
                    LOGGER.error("Error occurred in database server while inserting Jenkins build invoker as a Watcher", e);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error occurred while searching Jenkins build invoker in database server ", ex);
        }

    }

}