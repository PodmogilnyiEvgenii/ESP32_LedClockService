package com.home.ledclockservice.service;

import com.home.ledclockservice.dao.DeviceRepository;
import com.home.ledclockservice.dao.UniqueDeviceRepository;
import com.home.ledclockservice.model.Device;
import com.home.ledclockservice.model.UniqueDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DAOServices {
    private final DeviceRepository deviceRepository;
    private final UniqueDeviceRepository uniqueDeviceRepository;

    @Autowired
    public DAOServices(DeviceRepository deviceRepository, UniqueDeviceRepository uniqueDeviceRepository) {
        this.deviceRepository = deviceRepository;
        this.uniqueDeviceRepository = uniqueDeviceRepository;
    }

    public void saveDevice(Device device) {
        deviceRepository.save(device);
    }

    public Device findLastDevice(String deviceId) {
        return deviceRepository.findFirstByDeviceIdOrderByDataIdDesc(deviceId);
    }

    public List<String> find5LastMessages(String deviceId) {
        return deviceRepository.findTop5ByDeviceIdOrderByDataIdDesc(deviceId).stream().map(Device::getLastMessage).toList();
    }

    public UniqueDevice findUniqueDevice (String deviceId) {
        return uniqueDeviceRepository.findFirstByDeviceId (deviceId);
    }

    public void saveUniqueDevice(UniqueDevice uniqueDevice) {
        uniqueDeviceRepository.save(uniqueDevice);
    }

    public String findMessage(int dataId) {
        return deviceRepository.findFirstByDataId(dataId).getLastMessage();
    }
}

