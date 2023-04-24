package com.home.ledclockservice.model;

import com.home.ledclockservice.AppContext;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
public class Devices {

    private List<Device> devices = new ArrayList<>();


    public List<Device> getDevices() {
        return devices;
    }

    public List<Device> addDevice(Device device) {
        devices.add(device);
        return devices;
    }

    public List<Device> removeDevice(Device device) {
        devices.remove(device);
        return devices;
    }

    public int amountDevice() {
        return devices.size();
    }

    @PostConstruct
    private void postInit1() {
        System.out.println("Post init devices1");
        Device device = new Device("E48E0D84", "home","{\"id\":\"E48E0D84\",\"name\":\"home\",\"type\":\"data\",\"year\":2023,\"month\":4,\"day\":23,\"hour\":13,\"minute\":28,\"second\":0,\"lon\":\"30.44\",\"lat\":\"59.92\",\"weather\":\"clear sky\",\"temp\":14.45,\"pressure\":756,\"humidity\":54,\"wind speed\":4,\"lastTimeGetWeather\":472,\"saveDataStatus\":\"good\"}" );
        devices.add(device);
        devices.add(device);
        devices.add(device);
    }

    @PostConstruct
    private void postInit2() {
        System.out.println("Post init devices2");
        Device device = new Device("E48E0D84", "home","{\"id\":\"E48E0D84\",\"name\":\"home\",\"type\":\"data\",\"year\":2023,\"month\":4,\"day\":23,\"hour\":13,\"minute\":28,\"second\":0,\"lon\":\"30.44\",\"lat\":\"59.92\",\"weather\":\"clear sky\",\"temp\":14.45,\"pressure\":756,\"humidity\":54,\"wind speed\":4,\"lastTimeGetWeather\":472,\"saveDataStatus\":\"good\"}" );
        devices.add(device);
        devices.add(device);
        devices.add(device);
        //AppContext.getAppContext().getBean("devices", Devices.class).getDevices().add(device);
    }
}
