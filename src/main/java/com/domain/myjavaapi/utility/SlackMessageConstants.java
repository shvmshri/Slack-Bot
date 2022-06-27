package com.domain.myjavaapi.utility;

public class SlackMessageConstants {
    public static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    public static final String SLACK_SIGNING_SECRET = System.getenv("SLACK_SIGNING_SECRET");
    public static final String HELP_TEXT = "The following commands can be used via : \n /watch chartName{chartName} releaseName{releaseName} time{time to watch either in hours or minutes, use h or m characters to define the same} \n e.g. /watch care qa4-tier1 2h \n /unwatch chartName{chartName} releaseName{releaseName} \n e.g. /unwatch care qa4-tier1 \n /users_list chartName{chartName} releaseName{releaseName}  \n e.g. /users_list care qa4-tier1 \n";
    public static String SUCCESS_WATCH = "You are successfully added to the Watcher's list.";
    public static String FAILURE = "You are successfully added to the Watcher's list.";
    public static String SUCCESS_UNWATCH = "You are successfully removed from the Watcher's list";
    public static String NOT_A_WATCHER = "You first have to watch a specific chart and release to use unwatch functionality.";
    public static String NO_WATCHERS = "No one is watching the branch";
    public static String WATCHERS_LIST = "The list of users watching the branch are ";
    public static String THANK_YOU = "ThankYou for the data";
    public static String INVALID_ARGS = "Invalid Arguments! Use \"/demo_app help\" command to launch a help desk";
    public static String WAIT = "Wait! Fetching your data";

}
