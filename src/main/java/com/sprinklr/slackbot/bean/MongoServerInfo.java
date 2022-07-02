package com.sprinklr.slackbot.bean;

public class MongoServerInfo {
    private String url;
    private String dbName;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}