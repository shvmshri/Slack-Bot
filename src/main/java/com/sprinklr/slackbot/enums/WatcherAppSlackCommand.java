package com.sprinklr.slackbot.enums;

public enum WatcherAppSlackCommand {
    ADD_WATCHER("/watch", 3),
    REMOVE_WATCHER("/unwatch", 2),
    WATCHERS_LIST("/watchers_list", 2),
    HELP("/watcher", 1);

    private String command;
    private int numArgs;


    WatcherAppSlackCommand(String command, int numArgs) {
        this.command = command;
        this.numArgs = numArgs;
    }

    public String getCommand() {
        return command;
    }

    public int getNumArgs() {
        return numArgs;
    }


}