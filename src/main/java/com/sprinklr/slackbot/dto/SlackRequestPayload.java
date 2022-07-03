package com.sprinklr.slackbot.dto;

import com.google.gson.annotations.SerializedName;

public class SlackRequestPayload {
    @SerializedName(value = "action_id")
    private String actionId;
    private String value;
    private View view;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getPrivateMetadata() {
        return getView().getPrivateMetadata();
    }

    private class View {
        @SerializedName(value = "private_metadata")
        private String privateMetadata;

        public String getPrivateMetadata() {
            return privateMetadata;
        }

        public void setPrivateMetadata(String privateMetadata) {
            this.privateMetadata = privateMetadata;
        }
    }
}