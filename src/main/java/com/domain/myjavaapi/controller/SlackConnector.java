package com.domain.myjavaapi.controller;

import com.domain.myjavaapi.enums.WatcherAppSlackCommand;
import com.domain.myjavaapi.services.SlackCommandService;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import com.domain.myjavaapi.utility.Utils;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SlackConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackConnector.class);
    private static SlackCommandService slackService;


    private static App createAndConfigApp() {

        if (SlackMessageConstants.SLACK_SIGNING_SECRET == null) {
            LOGGER.error("[SLACK_CONNECTOR_CRITICAL] SLACK_SIGNING_SECRET is null");
        }
        if (SlackMessageConstants.SLACK_BOT_TOKEN == null) {
            LOGGER.error("[SLACK_CONNECTOR_CRITICAL] SLACK_BOT_TOKEN is null");
        }
        AppConfig config = AppConfig.builder()
                .signingSecret(SlackMessageConstants.SLACK_SIGNING_SECRET)
                .singleTeamBotToken(SlackMessageConstants.SLACK_BOT_TOKEN)
                .build();

        App app = new App(config);
        addAndConfigureWatcherAppCommands(app);

        return app;

    }

    private static void addAndConfigureWatcherAppCommands(App app) {
        for (WatcherAppSlackCommand watcherAppSlackCommand : WatcherAppSlackCommand.values()) {
            app.command(watcherAppSlackCommand.getCommand(), (req, ctx) -> {

                String commandArgText = req.getPayload().getText();
                if (Utils.numArgs(commandArgText) != watcherAppSlackCommand.getNumArgs()) {
                    return ctx.ack(SlackMessageConstants.INVALID_ARGS_NUM);
                }

                String userId = req.getPayload().getUserId();
                User user = ctx.client().usersInfo(r -> r.token(ctx.getBotToken()).user(userId)).getUser();

                app.executorService().submit(() -> {
                    callSlackService(watcherAppSlackCommand, commandArgText, user);
                });

                return ctx.ack(watcherAppSlackCommand.getAckText());

            });
        }
    }

    private static void callSlackService(WatcherAppSlackCommand watcherAppSlackCommand, String commandArgText, User user) {
        switch (watcherAppSlackCommand) {

            case ADD_WATCHER:
                slackService.handleWatchCommand(commandArgText, user.getId(), user.getProfile().getEmail());
                break;

            case REMOVE_WATCHER:
                slackService.handleUnwatchCommand(commandArgText, user.getId());
                break;

            case WATCHERS_LIST:
                slackService.handleWatcherListCommand(commandArgText, user.getId());
                break;

            default:
                break;
        }
    }


    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.domain.myjavaapi");
        slackService = ctx.getBean(SlackCommandService.class);

        App app = createAndConfigApp();
        SlackAppServer server = new SlackAppServer(app);
        try {
            server.start();   // http://localhost:3000/slack/events
        } catch (Exception ex) {
            LOGGER.error("[SlackAPI_CRITICAL] Exception while starting the server for Slack App", ex);
        }

    }

}