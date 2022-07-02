package com.sprinklr.slackbot.app;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;

import com.sprinklr.slackbot.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlackAppBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackAppBuilder.class);
    @Autowired
    private WatcherAppBuilder watcherAppBuilder;

    public App createApp() {

        if (AppProperties.SLACK_SIGNING_SECRET == null) {
            LOGGER.error("[SLACK_APP_BUILDER_CRITICAL] SLACK_SIGNING_SECRET is null");
        }
        if (AppProperties.SLACK_BOT_TOKEN == null) {
            LOGGER.error("[SLACK_APP_BUILDER_CRITICAL] SLACK_BOT_TOKEN is null");
        }

        AppConfig config = AppConfig.builder()
                .signingSecret(AppProperties.SLACK_SIGNING_SECRET)
                .singleTeamBotToken(AppProperties.SLACK_BOT_TOKEN)
                .build();

        App app = new App(config);
        watcherAppBuilder.addAndConfigureCommands(app);

        return app;
    }

}
