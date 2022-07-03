package com.sprinklr.slackbot.enums;

public enum WatcherAppSlackCommand {
    ADD_WATCHER("/watch", "WATCH_CHART", "WATCH_CHART_BLOCK", "WATCH_RELEASES ", "WATCH_RELEASES_BLOCK", "WATCH", "Watch Server"),
    REMOVE_WATCHER("/unwatch", "UNWATCH_CHART", "UNWATCH_CHART_BLOCK", "UNWATCH_RELEASE", "UNWATCH_RELEASE_BLOCK", "UNWATCH", "UnWatch Server"),
    WATCHERS_LIST("/watchers_list", "WATCHERS_LIST_CHART", "WATCHERS_LIST_CHART_BLOCK", "WATCHERS_LIST_RELEASE", "WATCHERS_LIST_RELEASE_BLOCK", "WATCHER_LIST", "Get list of watchers");


    private String command;
    private String chart;
    private String chartBlock;
    private String release;
    private String releaseBlock;
    private String view;
    private String title;

    WatcherAppSlackCommand(String command, String chart, String chartBlock, String release, String releaseBlock, String view, String title) {
        this.command = command;
        this.chart = chart;
        this.chartBlock = chartBlock;
        this.release = release;
        this.releaseBlock = releaseBlock;
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

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getChartBlock() {
        return chartBlock;
    }

    public void setChartBlock(String chartBlock) {
        this.chartBlock = chartBlock;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getReleaseBlock() {
        return releaseBlock;
    }

    public void setReleaseBlock(String releaseBlock) {
        this.releaseBlock = releaseBlock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}