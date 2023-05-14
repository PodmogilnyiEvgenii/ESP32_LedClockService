package com.home.ledclockservice.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDataType {
    @JsonProperty("id")
    private String deviceId;
    private String name;
    private String type;
}
