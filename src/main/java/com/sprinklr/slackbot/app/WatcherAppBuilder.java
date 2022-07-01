package com.sprinklr.slackbot.app;

import com.slack.api.bolt.App;
import com.slack.api.model.User;
import com.sprinklr.slackbot.enums.WatcherAppSlackCommand;
import com.sprinklr.slackbot.service.WatcherCommandService;
import com.sprinklr.slackbot.util.WatcherAppMessageConstants;
import com.sprinklr.slackbot.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WatcherAppBuilder {

    @Autowired
    private WatcherCommandService watcherCommandService;
    private static final String HELP = "help";


    public void addAndConfigureCommands(App app) {
        for (WatcherAppSlackCommand watcherAppSlackCommand : WatcherAppSlackCommand.values()) {
            app.command(watcherAppSlackCommand.getCommand(), (req, ctx) -> {

                String commandArgText = req.getPayload().getText();
                if (Utils.numArgs(commandArgText) != watcherAppSlackCommand.getNumArgs()) {
                    return ctx.ack(WatcherAppMessageConstants.INVALID_ARGS_NUM);
                }

                String userId = req.getPayload().getUserId();
                User user = ctx.client().usersInfo(r -> r.token(ctx.getBotToken()).user(userId)).getUser();

                String message = callSlackService(watcherAppSlackCommand, commandArgText, user);

                return ctx.ack(message);

            });
        }
    }

    private String callSlackService(WatcherAppSlackCommand watcherAppSlackCommand, String commandArgText, User user) {
        switch (watcherAppSlackCommand) {

            case ADD_WATCHER:
                return watcherCommandService.handleWatchCommand(commandArgText, user.getId(), user.getProfile().getEmail());

            case REMOVE_WATCHER:
                return watcherCommandService.handleUnwatchCommand(commandArgText, user.getId());

            case WATCHERS_LIST:
                return watcherCommandService.handleWatcherListCommand(commandArgText, user.getId());

            case HELP:
                if (!commandArgText.equalsIgnoreCase(HELP)) {
                    return WatcherAppMessageConstants.INVALID_ARGS;
                }
                return WatcherAppMessageConstants.HELP_TEXT;

            default:
                return null;
        }
    }
}
