package com.home.ledclockservice.dto;

import com.home.ledclockservice.model.RawData;
import lombok.Data;

@Data
public class DeviceDto {
    private String deviceId;
    private String name;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String longitude;
    private String latitude;
    private String descriptionWeather;
    private String temperature;
    private int pressure;
    private int humidity;
    private int windSpeed;
    private int lastTimeGetWeather;
    private String saveDataStatus;

    public DeviceDto(RawData rawData) {
        if (rawData != null) {
            this.deviceId = rawData.getDeviceId();
            this.name = rawData.getName();
            this.year = rawData.getYear();
            this.month = rawData.getMonth();
            this.day = rawData.getDay();
            this.hour = rawData.getHour();
            this.minute = rawData.getMinute();
            this.second = rawData.getSecond();
            this.longitude = String.format("%.2f", rawData.getLongitude());
            this.latitude = String.format("%.2f", rawData.getLatitude());
            this.descriptionWeather = rawData.getDescriptionWeather();
            this.temperature = String.format("%.2f", rawData.getTemperature());
            this.pressure = rawData.getPressure();
            this.humidity = rawData.getHumidity();
            this.windSpeed = rawData.getWindSpeed();
            this.lastTimeGetWeather = rawData.getLastTimeGetWeather();
            this.saveDataStatus = rawData.getSaveDataStatus();
        }
    }
}
