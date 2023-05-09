package com.home.ledclockservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.ledclockservice.dao.DeviceRepository;
import com.home.ledclockservice.model.Device;
import com.home.ledclockservice.model.UniqueDevice;
import com.home.ledclockservice.model.UniqueDeviceForView;
import com.home.ledclockservice.dao.UniqueDeviceRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Configuration
public class DeviceServices {
    private DAOServices daoServices;
    private DeviceRepository deviceRepository;
    private UniqueDeviceRepository uniqueDeviceRepository;
    private List<UniqueDevice> onlineDevices = new ArrayList<>();
    private List<UniqueDevice> offlineDevices = new ArrayList<>();
    private Timer timer = new Timer();

    @Autowired
    public DeviceServices(DAOServices daoServices, DeviceRepository deviceRepository, UniqueDeviceRepository uniqueDeviceRepository) {
        this.daoServices = daoServices;
        this.deviceRepository = deviceRepository;
        this.uniqueDeviceRepository = uniqueDeviceRepository;
    }

    public boolean addDevice(List<UniqueDevice> uniqueDevices, UniqueDevice device) {
        if (device != null) {
            for (UniqueDevice findDevice : uniqueDevices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    log.debug("Device add error (duplicated)");
                    return false;
                }
            }
            device.createTimer();
            uniqueDevices.add(device);
            return true;
        } else {
            log.debug("Device add error (device is null)");
            return false;
        }
    }

    public boolean updateDevice(List<UniqueDevice> uniqueDevices, UniqueDevice device) {
        if (device != null) {
            List<UniqueDevice> copyOnlineDevices = new ArrayList<>(uniqueDevices);
            for (UniqueDevice findDevice : copyOnlineDevices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    uniqueDevices.remove(findDevice);
                    device.createTimer();
                    uniqueDevices.add(device);
                    return true;
                }
            }
            log.debug("Device update error (not found)");
        } else {
            log.debug("Device update error (device is null)");
        }
        return false;
    }

    public boolean removeDevice(List<UniqueDevice> uniqueDevices, UniqueDevice device) {
        if (device != null) {
            List<UniqueDevice> copyOnlineDevices = new ArrayList<>(uniqueDevices);
            for (UniqueDevice findDevice : copyOnlineDevices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    uniqueDevices.remove(findDevice);
                    return true;
                }
            }
            log.debug("Device remove error (not found)");
        } else {
            log.debug("Device remove error (device is null)");
        }
        return false;
    }

      public Device getDeviceFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Device device = objectMapper.readValue(jsonString, Device.class);
            device.setLastMessage(jsonString);
            if (device.getDeviceId() == null || device.getName() == null) throw new IllegalArgumentException();
            return device;

        } catch (JsonProcessingException | IllegalArgumentException e) {
            log.debug("Device parse from JSON error: {}", e.toString());
            return null;
        }
    }

    public void checkDeviceStatus() {
        List<UniqueDevice> copyOnlineDevices = new ArrayList<>(onlineDevices);
        for (UniqueDevice uniqueDevice : copyOnlineDevices) {
            if (!uniqueDevice.isOnline()) {
                onlineDevices.remove(uniqueDevice);
                offlineDevices.add(uniqueDevice);
            }
        }

        List<UniqueDevice> copyOfflineDevices = new ArrayList<>(offlineDevices);
        for (UniqueDevice uniqueDevice : copyOfflineDevices) {
            if (uniqueDevice.isOnline()) {
                offlineDevices.remove(uniqueDevice);
                onlineDevices.add(uniqueDevice);
            }
        }
    }

    public List<UniqueDeviceForView> uniqueToUniqueForView(List<UniqueDevice> uniqueDeviceList) {
        List<UniqueDeviceForView> uniqueDevicesToViewList = new ArrayList<>();

        for (UniqueDevice uniqueDevice : uniqueDeviceList) {
            uniqueDevicesToViewList.add(new UniqueDeviceForView(uniqueDevice, uniqueDevice.isOnline(), daoServices.findMessage(uniqueDevice.getLastDataId())));
        }
        uniqueDevicesToViewList.sort(Comparator.comparingInt(UniqueDeviceForView::getId));
        return uniqueDevicesToViewList;
    }

    @PostConstruct
    private void Init() {
        offlineDevices = uniqueDeviceRepository.findAll();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkDeviceStatus();
            }
        }, 0, TimeUnit.MINUTES.toMillis(1));
    }
}
