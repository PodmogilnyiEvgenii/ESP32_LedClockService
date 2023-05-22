package com.home.ledclockservice.service;

import com.home.ledclockservice.dao.RawDataRepository;
import com.home.ledclockservice.dao.DeviceRepository;
import com.home.ledclockservice.model.RawData;
import com.home.ledclockservice.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DAOServices {
    private final RawDataRepository rawDataRepository;
    private final DeviceRepository deviceRepository;

    public void saveDevice(RawData rawData) {
        rawDataRepository.save(rawData);
    }

    public RawData findLastDevice(String deviceId) {
        return rawDataRepository.findFirstByDeviceIdOrderByIdDesc(deviceId);
    }

    public List<String> find5LastMessages(String deviceId) {
        return rawDataRepository.findTop5ByDeviceIdOrderByIdDesc(deviceId).stream().map(RawData::getLastMessage).toList();
    }

    public Device findUniqueDevice(String deviceId) {
        return deviceRepository.findFirstByDeviceId(deviceId);
    }

    public Device UpdateUniqueDevice(RawData rawData) {
        RawData lastRawData = findLastDevice(rawData.getDeviceId());   //last dataId
        Device device = findUniqueDevice(rawData.getDeviceId());

        if (device == null) {
            device = new Device();
            device.setDeviceId(lastRawData.getDeviceId());
        }

        device.setName(lastRawData.getName());
        device.setLastDataId(lastRawData.getId());

        saveUniqueDevice(device);
        return device;
    }

    public void saveUniqueDevice(Device device) {
        deviceRepository.save(device);
    }

    public String findMessage(int dataId) {
        return rawDataRepository.findFirstById(dataId).getLastMessage();
    }
}

