package com.domain.myjavaapi.services;

import com.domain.myjavaapi.utility.SlackMessageConstants;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlackMessageDispatchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackMessageDispatchingService.class);

    public void slackSendMsg(String userId, String message) {

        MethodsClient client = Slack.getInstance().methods();
        try {
            client.chatPostMessage((r -> r.token(SlackMessageConstants.SLACK_BOT_TOKEN).channel(userId).text(message)));
        } catch (Exception e) {
            LOGGER.error("Exception occurred while sending message to Slack user.",e);
        }

    }

    public void slackSendMsg( List<String> userIDList, String message){

        try{
            for (String userID : userIDList) {
                slackSendMsg(userID,message);
            }
        } catch (Exception e){
            LOGGER.error("Error occurred while sending notification about the build to Watchers through Slack",e);
        }

    }

}