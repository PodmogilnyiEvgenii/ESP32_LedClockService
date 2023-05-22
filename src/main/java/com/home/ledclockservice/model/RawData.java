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
public class RawData {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private int id;

    @JsonProperty("id")
    @Column
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

    @JsonProperty("lat")
    @Column
    private double latitude;
    @JsonProperty("lon")
    @Column
    private double longitude;

    @JsonProperty("weather")
    @Column
    private String descriptionWeather;

    @JsonProperty("temp")
    @Column
    private double temperature;

    @Column
    private int pressure;

    @Column
    private int humidity;

    @JsonProperty
    @Column
    private int windSpeed;

    @Column
    private int lastTimeGetWeather;

    @Column
    private String saveDataStatus;

    @JsonIgnore
    @Column
    private String lastMessage;
}
