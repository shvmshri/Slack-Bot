package com.domain.myjavaapi.interactors;

import com.domain.myjavaapi.handlers.CommandHandler;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SlackServerConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackServerConnector.class);

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.domain.myjavaapi");
        CommandHandler slackCommandHandler = ctx.getBean(CommandHandler.class);

        App app = slackCommandHandler.createAndConfigApp();
        SlackAppServer server = new SlackAppServer(app);
        try {
            server.start();   // http://localhost:3000/slack/events
        } catch (Exception ex) {
            LOGGER.error("[SlackAPI_CRITICAL] Exception while starting the server for Slack App", ex);
        }

    }
}