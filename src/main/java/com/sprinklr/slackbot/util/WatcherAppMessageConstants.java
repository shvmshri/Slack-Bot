package com.sprinklr.slackbot.util;

public class WatcherAppMessageConstants {

    public static final String HELP_TEXT = "The following commands can be used : \n\n 1. `/watch {chart-name} {release-name} {duration}` \n _This command will add you to the watchers list of the server provided and for the duration given by the user. All the watchers of the server will be informed if someone triggers a build on that specific server._ \n _Usage Examples:_ `/watch care qa4-tier1 2h`  `/watch care qa4-tier1 2m`  \n\n\n 2. `/unwatch {chart-name} {release-name}` \n _This command will remove the user from the watchers's list of the server provided._ \n _Usage Example:_ `/unwatch care qa4-tier1`  \n\n\n 3.  `/watchers_list {chart-name} {release-name}`  \n _This command will display the list of watchers watching the server provided._ \n _Usage Example:_  `/watchers_list care qa4-tier1`";
    public static final String SUCCESS_WATCH = "You are successfully added to the Watcher's list.";
    public static final String FAILURE = "Your request failed due to some error. Please try again!";
    public static final String SUCCESS_UNWATCH = "You are successfully removed from the Watcher's list.";
    public static final String NOT_A_WATCHER = "You first have to watch a specific chart and release to use unwatch functionality.";
    public static final String NO_WATCHERS = "No one is watching the server.";
    public static final String WATCHERS_LIST = "The list of users watching the server : \n";
    public static final String THANK_YOU = "Hurray! We received your request.";
    public static final String INVALID_ARGS_NUM = "Invalid number of Arguments! Use \"/watcher help\" command to launch a help desk.";
    public static final String INVALID_ARGS = "Invalid Arguments! Use \"/watcher help\" command to launch a help desk.";
    public static final String INVALID_ARGS_VALUE = "Provided values of chartName or releaseName are invalid. Please provide valid names to proceed further.";
    public static final String INVALID_TIME = "Invalid format of Time! Use \"/watcher help\" command to launch a help desk.";
    public static final String SUCCESS_JENKINS_AS_WATCHER = "Hey, we have added you to our Watcher's List for the server you triggered a build for. This will help you prevent clashes and will notify you if any other user try to invoke a build on the same server.";

}