package com.sprinklr.slackbot.bean;

import com.sprinklr.slackbot.util.Utils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Watcher")
public class Watcher {
    public static String ID = "id";
    public static String CHART_NAME = "chartName";
    public static String RELEASE_NAME = "releaseName";
    public static String USER_ID = "userId";
    public static String USER_EMAIL = "userEmail";
    public static String DURATION = "duration";
    public static String EXPIRE_AT = "expireAt";
    public static String COLLECTION = "Watcher";

    @Id
    private String id;
    private String chartName;
    private String releaseName;
    private String userId;
    private String userEmail;
    private String duration;

    private Date expireAt;


    public Watcher() {
    }

    public Watcher(String chart, String release, String duration, String userId, String userEmail) {
        this.chartName = chart;
        this.releaseName = release;
        this.duration = duration;
        this.userId = userId;
        this.userEmail = userEmail;
        this.expireAt = Utils.findExpireDate(duration);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}