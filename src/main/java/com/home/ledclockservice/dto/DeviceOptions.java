package com.home.ledclockservice.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceOptions {
    private String id;
    private String name;
    private String type;

    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lon")
    private double longitude;

    private int dayBrightness;
    private int nightBrightness;

    private int dayR;
    private int dayG;
    private int dayB;

    private int nightR;
    private int nightG;
    private int nightB;

    private int dayStart;
    private int nightStart;

    private String appid;
    private String ntpServer;
}
