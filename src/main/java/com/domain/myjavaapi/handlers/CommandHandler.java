package com.domain.myjavaapi.handlers;

import com.domain.myjavaapi.services.WatcherSlackService;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import com.domain.myjavaapi.utility.Utils;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);


    @Autowired
    private WatcherSlackService slackService;

    public App createAndConfigApp() {
        if (SlackMessageConstants.SLACK_BOT_TOKEN == null || SlackMessageConstants.SLACK_SIGNING_SECRET == null) {
            LOGGER.error("Unable to find Authentication tokens for Slack App");
        }
        AppConfig config = AppConfig.builder()
                .signingSecret(SlackMessageConstants.SLACK_SIGNING_SECRET)
                .singleTeamBotToken(SlackMessageConstants.SLACK_BOT_TOKEN)
                .build();

        App app = new App(config);
        addWatchCommand(app);
        addUnwatchCommand(app);
        addUserListCommand(app);
        addHelpCommand(app);
        return app;
    }

    public void addWatchCommand(App app) {
        app.command("/watch", (req, ctx) -> {
            String commandArgText = req.getPayload().getText();
            String userId = req.getPayload().getUserId();
            User user = ctx.client().usersInfo(r -> r.token(ctx.getBotToken()).user(userId)).getUser();
            String userEmail = user.getProfile().getEmail();
            int numArg = Utils.numArgCheck(commandArgText);
            //If the args were valid then doing further async computations

            if (numArg == 3) {
                app.executorService().submit(() -> {
                    //Mongo async storage as token has to  be passed within 3 secs
                    slackService.handleWatchCommand(commandArgText, userId, userEmail);
                });
            }
             String text = (numArg == 3 ) ? SlackMessageConstants.THANK_YOU : SlackMessageConstants.INVALID_ARGS;

            return ctx.ack(text);
        });

    }

    public void addUnwatchCommand(App app) {
        app.command("/unwatch", (req, ctx) -> {
            String commandArgText = req.getPayload().getText();
            String userId = req.getPayload().getUserId();

            int check = Utils.numArgCheck(commandArgText);
            //If the args were valid then doing further async computations
            if (check == 2) {
                app.executorService().submit(() -> {
                    //Mongo async storage as token has to  be passed within 3 secs
                    slackService.handleUnwatchCommand(commandArgText, userId);
                });
            }
            String text = (check == 2) ? SlackMessageConstants.THANK_YOU : SlackMessageConstants.INVALID_ARGS;
            return ctx.ack(text); // respond with 200 OK

        });
    }

    public void addUserListCommand(App app) {

        app.command("/users_list", (req, ctx) -> {
            String commandArgText = req.getPayload().getText();
            String userId = req.getPayload().getUserId();

            int check = Utils.numArgCheck(commandArgText);

            //If the args were valid then doing further async computations
            if (check == 2) {
                app.executorService().submit(() -> {
                    //Mongo async storage as token has to  be passed within 3 secs
                    slackService.handleWatcherListCommand(commandArgText, userId);
                });
            }

            String text = (check == 2) ? SlackMessageConstants.WAIT : SlackMessageConstants.INVALID_ARGS;
            return ctx.ack(text); // respond with 200 OK

        });

    }

    public void addHelpCommand(App app){

        app.command("/demo_app", (req, ctx)->{
            String commandArgText = req.getPayload().getText();

            int check = Utils.numArgCheck(commandArgText);

            String text = null;
            if (check == 1 && commandArgText.equalsIgnoreCase("Help")) {
                text = SlackMessageConstants.HELP_TEXT;
            }
            else text = SlackMessageConstants.INVALID_ARGS;
            return ctx.ack(text);

        });
    }

}
