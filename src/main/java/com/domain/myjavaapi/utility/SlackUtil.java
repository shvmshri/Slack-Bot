package com.domain.myjavaapi.utility;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackUtil.class);
    public static String findSlackUserId(String userEmail) {

        MethodsClient client = Slack.getInstance().methods();
        String userSlackId = null;
        try {
            UsersLookupByEmailResponse user = client.usersLookupByEmail((r->r.token(SlackMessageConstants.SLACK_BOT_TOKEN).email(userEmail)));
            userSlackId = user.getUser().getId();

        } catch (Exception e) {
            LOGGER.error("Exception occurred while finding the following user on slack.", e);
        }
        return userSlackId;
    }
}
