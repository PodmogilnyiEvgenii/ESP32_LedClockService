package com.home.ledclockservice.model;

import lombok.Data;

@Data
public class UniqueDeviceForView  {
    private int id;
    private String deviceId;
    private String name;
    private int lastDataId;
    private String lastMessage;
    private String status;

    public UniqueDeviceForView(UniqueDevice uniqueDevice, boolean isOnline, String lastMessage) {
        this.id=uniqueDevice.getId();
        this.deviceId=uniqueDevice.getDeviceId();
        this.name=uniqueDevice.getName();
        this.lastDataId=uniqueDevice.getLastDataId();
        this.lastMessage = lastMessage;
        this.status = isOnline ? "online" : "offline";
    }
}
