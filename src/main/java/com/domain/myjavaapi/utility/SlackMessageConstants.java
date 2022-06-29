package com.domain.myjavaapi.utility;

public class SlackMessageConstants {
    public static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    public static final String SLACK_SIGNING_SECRET = System.getenv("SLACK_SIGNING_SECRET");
    public static String HELP_TEXT = "The following commands can be used : \n\n /watch chartName{chartName} releaseName{releaseName} time{time to watch either in hours or minutes, use h or m characters to define the same} \n e.g. /watch care qa4-tier1 2h \n This command will add you to the watchers list for that particular branch provided. All the watchers of the branch will be informed if someone triggers a build on that specific branch. The user will be automatically removed from the watcher's list after the time provided in the command. \n\n /unwatch chartName{chartName} releaseName{releaseName} \n e.g. /unwatch care qa4-tier1 \n  This command will remove the user from the watchers's list of the branch provided \n\n /users_list chartName{chartName} releaseName{releaseName}  \n e.g. /users_list care qa4-tier1 \n This command will display the list of watchers of that specific branch.\n";
    public static String SUCCESS_WATCH = "You are successfully added to the Watcher's list.";
    public static String FAILURE = "You are successfully added to the Watcher's list.";
    public static String SUCCESS_UNWATCH = "You are successfully removed from the Watcher's list";
    public static String NOT_A_WATCHER = "You first have to watch a specific chart and release to use unwatch functionality.";
    public static String NO_WATCHERS = "No one is watching the branch";
    public static String WATCHERS_LIST = "The list of users watching the branch are ";
    public static String THANK_YOU = "ThankYou for the data";
    public static String INVALID_ARGS = "Invalid Arguments! Use \"/demo_app help\" command to launch a help desk";
    public static String WAIT = "Wait! Fetching your data";
    public static String SUCCESS_JENKINS_AS_WATCHER = "Hey, we have added you to our Watcher's List for the branch you triggered a build for. This will help you prevent clashes and will notify you if any other user try to invoke a build on the same branch.";

}