package com.home.ledclockservice.dto;

import com.home.ledclockservice.model.Device;
import lombok.Data;

@Data
public class UniqueDeviceDto {
    private int id;
    private String deviceId;
    private String name;
    private int lastDataId;
    private String lastMessage;
    private String status;

    public UniqueDeviceDto(Device device, boolean isOnline, String lastMessage) {
        this.id= device.getId();
        this.deviceId= device.getDeviceId();
        this.name= device.getName();
        this.lastDataId= device.getLastDataId();
        this.lastMessage = lastMessage;
        this.status = isOnline ? "online" : "offline";
    }
}
