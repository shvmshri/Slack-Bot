package com.domain.myjavaapi.models;

import com.domain.myjavaapi.utility.Utils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Watcher")
public class Watcher {
    public static String ID = "id";
    public static String CHARTNAME = "chartName";
    public static String RELEASENAME = "releaseName";
    public static String USERID = "userId";
    public static String USEREMAIL = "userEmail";
    public static String TIME = "time";
    public static String EXPIREAT = "expireAt";
    public static String COLLECTION = "Watcher";

    @Id
    private String id;
    private String chartName;
    private String releaseName;
    private String userId;
    private String userEmail;
    private String time;

    private Date expireAt;


    public Watcher() {
    }

    public Watcher(String chart, String release, String time, String userId, String userEmail) {
        this.chartName = chart;
        this.releaseName = release;
        this.time = time;
        this.userId = userId;
        this.userEmail = userEmail;
        this.expireAt = Utils.findExpireDate(time);
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

    public String getUserEmail() {return userEmail;}

    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}