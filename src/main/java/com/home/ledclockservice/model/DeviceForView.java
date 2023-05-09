package com.home.ledclockservice.model;

import lombok.Data;

@Data
public class DeviceForView {
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

    public DeviceForView(Device device) {
        this.deviceId = device.getDeviceId();
        this.name = device.getName();
        this.year = device.getYear();
        this.month = device.getMonth();
        this.day = device.getDay();
        this.hour = device.getHour();
        this.minute = device.getMinute();
        this.second = device.getSecond();
        this.longitude = String.format("%.2f",device.getLongitude());
        this.latitude = String.format("%.2f",device.getLatitude());
        this.descriptionWeather = device.getDescriptionWeather();
        this.temperature = String.format("%.2f",device.getTemperature());
        this.pressure = device.getPressure();
        this.humidity = device.getHumidity();
        this.windSpeed = device.getWindSpeed();
        this.lastTimeGetWeather = device.getLastTimeGetWeather();
        this.saveDataStatus = device.getSaveDataStatus();
    }
}
