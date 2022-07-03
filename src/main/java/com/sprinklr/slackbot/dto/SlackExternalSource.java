package com.sprinklr.slackbot.dto;

public class SlackExternalSource {
    private class Options {
        private class Text {
            private String type = "plain_text";
            private String text;

            public Text(String data) {
                text = data;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        private Text text;
        private String value;

        public Options(int index, String data) {
            value = "value-" + index;
            text = new Text(data);
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private Options[] options;

    public SlackExternalSource(int size) {
        options = new Options[size];
    }

    public void addOptions(int index, String data) {
        options[index] = new Options(index, data);
    }

    public Options[] getOptions() {
        return options;
    }

    public void setOptions(Options[] options) {
        this.options = options;
    }
}