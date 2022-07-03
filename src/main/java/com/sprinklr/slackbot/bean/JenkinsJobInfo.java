package com.sprinklr.slackbot.bean;

public class JenkinsJobInfo {

    private String chartName;
    private String releaseName;
    private String userEmail;
    private String jobLink;

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

    public String getUserEmail() {
        return userEmail;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}