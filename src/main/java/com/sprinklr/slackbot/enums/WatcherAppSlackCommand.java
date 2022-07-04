package com.sprinklr.slackbot.enums;

public enum WatcherAppSlackCommand {
    ADD_WATCHER("/watch", "WATCH_CHART", "WATCH_CHART_BLOCK", "WATCH_RELEASES ", "WATCH_RELEASES_BLOCK", "WATCH_REPO", "WATCH_REPO_BLOCK", "WATCH", "Watch Server"),
    REMOVE_WATCHER("/unwatch", "UNWATCH_CHART", "UNWATCH_CHART_BLOCK", "UNWATCH_RELEASE", "UNWATCH_RELEASE_BLOCK", "UNWATCH_REPO", "UNWATCH_REPO_BLOCK", "UNWATCH", "UnWatch Server"),
    WATCHERS_LIST("/watchers_list", "WATCHERS_LIST_CHART", "WATCHERS_LIST_CHART_BLOCK", "WATCHERS_LIST_RELEASE", "WATCHERS_LIST_RELEASE_BLOCK", "WATCHERS_LIST_REPO", "WATCHERS_LIST_REPO_BLOCK", "WATCHER_LIST", "Get list of watchers");


    private String command;
    private String chartActionId;
    private String chartBlockId;
    private String releaseActionId;
    private String releaseBlockId;

    private String repoActionId;
    private String repoBlockId;
    private String view;
    private String title;

    WatcherAppSlackCommand(String command, String chartActionId, String chartBlockId, String releaseActionId, String releaseBlockId, String repoActionId, String repoBlockId, String view, String title) {
        this.command = command;
        this.chartActionId = chartActionId;
        this.chartBlockId = chartBlockId;
        this.releaseActionId = releaseActionId;
        this.releaseBlockId = releaseBlockId;
        this.repoActionId = repoActionId;
        this.repoBlockId = repoBlockId;
        this.view = view;
        this.title = title;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getChartActionId() {
        return chartActionId;
    }

    public void setChartActionId(String chartActionId) {
        this.chartActionId = chartActionId;
    }

    public String getChartBlockId() {
        return chartBlockId;
    }

    public void setChartBlockId(String chartBlockId) {
        this.chartBlockId = chartBlockId;
    }

    public String getReleaseActionId() {
        return releaseActionId;
    }

    public void setReleaseActionId(String releaseActionId) {
        this.releaseActionId = releaseActionId;
    }

    public String getReleaseBlockId() {
        return releaseBlockId;
    }

    public void setReleaseBlockId(String releaseBlockId) {
        this.releaseBlockId = releaseBlockId;
    }

    public String getRepoActionId() {
        return repoActionId;
    }

    public void setRepoActionId(String repoActionId) {
        this.repoActionId = repoActionId;
    }

    public String getRepoBlockId() {
        return repoBlockId;
    }

    public void setRepoBlockId(String repoBlockId) {
        this.repoBlockId = repoBlockId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}