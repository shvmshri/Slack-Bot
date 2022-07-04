package com.sprinklr.slackbot.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.sprinklr.slackbot.app.SlackAppBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SlackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackController.class);

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.sprinklr.slackbot");
        SlackAppBuilder slackAppBuilder = ctx.getBean(SlackAppBuilder.class);

        App app = slackAppBuilder.createApp();
        SlackAppServer server = new SlackAppServer(app);
        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("[SLACK_CONTROLLER_CRITICAL] Could not start server", e);
        }

    }
}
