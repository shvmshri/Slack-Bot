package com.domain.myjavaapi.enums;

import com.domain.myjavaapi.utility.SlackMessageConstants;

public enum SlackCommand {
    ADD_WATCHER("/watch", 3, SlackMessageConstants.THANK_YOU),
    REMOVE_WATCHER("/unwatch", 2, SlackMessageConstants.THANK_YOU),
    WATCHERS_LIST("/watchers_list", 2, SlackMessageConstants.THANK_YOU),
    HELP("/app_help", 0, SlackMessageConstants.HELP_TEXT);

    private String command;
    private int numArgs;
    private String ackText;

    SlackCommand(String command, int numArgs, String ackText) {
        this.command = command;
        this.numArgs = numArgs;
        this.ackText = ackText;
    }

    public String getCommand() {
        return command;
    }

    public int getNumArgs() {
        return numArgs;
    }

    public String getAckText() {
        return ackText;
    }

}