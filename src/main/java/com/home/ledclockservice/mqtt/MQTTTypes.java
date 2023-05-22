package com.home.ledclockservice.mqtt;

public enum MQTTTypes {
    DATA("data"), SET("set"), GET("get"), OPTIONS("options"), ERROR("error");
    private final String string;

    MQTTTypes(String string) {
        this.string = string;
    }

    public String getTitle() {
        return string;
    }
}
