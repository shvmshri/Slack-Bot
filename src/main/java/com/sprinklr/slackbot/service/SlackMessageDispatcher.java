package com.sprinklr.slackbot.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlackMessageDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackMessageDispatcher.class);
    public static String SLACK_BOT_TOKEN = "";


    public void sendMessage(String userId, String message) {

        MethodsClient client = Slack.getInstance().methods();
        try {
            String text = "Hey <@" + userId + ">, " + message;
            ChatPostMessageResponse result = client.chatPostMessage((r -> r.token(SLACK_BOT_TOKEN).channel(userId).text(text)));
            if (!result.isOk()) {
                LOGGER.error("Could not send message to user on slack");
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while sending message to Slack user.", e);
        }

    }

    public void sendMessage(List<String> userIDList, String message) {

        for (String userID : userIDList) {
            sendMessage(userID, message);
        }

    }

}