package com.sprinklr.slackbot.dto;

import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;

import java.util.ArrayList;
import java.util.List;

public class SlackOptions {

    private List<OptionObject> options;

    public SlackOptions(int size) {
        options = new ArrayList<>(size);
    }

    public void addOptions(int index, String data) {
        OptionObject optionObject = new OptionObject();
        optionObject.setValue("value-" + index);
        optionObject.setText(new PlainTextObject(data, true));
        options.add(optionObject);
    }

    public List<OptionObject> getOptions() {
        return options;
    }

    public void setOptions(List<OptionObject> options) {
        this.options = options;
    }
}