package com.home.ledclockservice.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(schema = "clock",name = "rawdata")
public class Device {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "dataid")
    private int dataId;
    @JsonProperty("id")
    @Column(name = "deviceid")
    private String deviceId;
    @Column
    private String name;

    @Column
    private int year;

    @Column
    private int month;

    @Column
    private int day;

    @Column
    private int hour;

    @Column
    private int minute;

    @Column
    private int second;

    @JsonProperty("lon")
    @Column
    private double longitude;

    @JsonProperty("lat")
    @Column
    private double latitude;

    @JsonProperty("weather")
    @Column(name = "descriptionweather")
    private String descriptionWeather;

    @JsonProperty("temp")
    @Column
    private double temperature;

    @Column
    private int pressure;

    @Column
    private int humidity;

    @JsonProperty("wind speed")
    @Column(name = "windspeed")
    private int windSpeed;

    @Column(name = "lasttimegetweather")
    private int lastTimeGetWeather;

    @Column(name = "savedatastatus")
    private String saveDataStatus;

    @JsonIgnore
    @Column(name = "lastmessage")
    private String lastMessage;


}
