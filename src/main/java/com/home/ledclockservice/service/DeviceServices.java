package com.home.ledclockservice.service;

import com.home.ledclockservice.dao.DeviceRepository;
import com.home.ledclockservice.dto.DeviceOptions;
import com.home.ledclockservice.dto.UniqueDeviceDto;
import com.home.ledclockservice.model.Device;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@Configuration
@RequiredArgsConstructor
public class DeviceServices {
    private final DAOServices daoServices;
    private final DeviceRepository deviceRepository;
    private List<Device> onlineDevices = new ArrayList<>();
    private List<Device> offlineDevices = new ArrayList<>();
    private DeviceOptions deviceOptions;

    public boolean addDevice(List<Device> devices, Device device) {
        if (device != null) {
            for (Device findDevice : devices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    log.debug("Device add error (duplicated)");
                    return false;
                }
            }
            device.getStatus().createOnlineTimer();
            devices.add(device);
            return true;
        } else {
            log.debug("Device add error (device is null)");
            return false;
        }
    }

    public boolean updateDevice(List<Device> devices, Device device) {
        if (device != null) {
            List<Device> copyOnlineDevices = new ArrayList<>(devices);
            for (Device findDevice : copyOnlineDevices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    devices.remove(findDevice);
                    device.getStatus().createOnlineTimer();
                    devices.add(device);
                    return true;
                }
            }
            log.debug("Device update error (not found)");
        } else {
            log.debug("Device update error (device is null)");
        }
        return false;
    }

    public boolean removeDevice(List<Device> devices, Device device) {
        if (device != null) {
            List<Device> copyOnlineDevices = new ArrayList<>(devices);
            for (Device findDevice : copyOnlineDevices) {
                if (findDevice.getDeviceId().equals(device.getDeviceId())) {
                    devices.remove(findDevice);
                    return true;
                }
            }
            log.debug("Device remove error (not found)");
        } else {
            log.debug("Device remove error (device is null)");
        }
        return false;
    }

    public boolean saveDeviceOptions(DeviceOptions deviceOptions) {
        if (deviceOptions != null) {
            this.deviceOptions = deviceOptions;
            return true;
        } else {
            return false;
        }
    }

    public void checkDeviceStatus() {
        List<Device> copyOnlineDevices = new ArrayList<>(onlineDevices);
        for (Device device : copyOnlineDevices) {
            if (!device.getStatus().isOnline()) {
                onlineDevices.remove(device);
                offlineDevices.add(device);
            }
        }

        List<Device> copyOfflineDevices = new ArrayList<>(offlineDevices);
        for (Device device : copyOfflineDevices) {
            if (device.getStatus().isOnline()) {
                offlineDevices.remove(device);
                onlineDevices.add(device);
            }
        }
    }

    public List<UniqueDeviceDto> uniqueToUniqueForView(List<Device> deviceList) {
        return deviceList
                .stream()
                .map(uniqueDevice -> new UniqueDeviceDto(uniqueDevice, uniqueDevice.getStatus().isOnline(), daoServices.findMessage(uniqueDevice.getLastDataId())))
                .sorted(Comparator.comparingInt(UniqueDeviceDto::getId))
                .collect(Collectors.toList());
    }

    public void makeDeviceOnline(Device device) {
        if (!updateDevice(getOnlineDevices(), device)) {
            addDevice(getOnlineDevices(), device);
        }
        removeDevice(getOfflineDevices(), device);
    }

    public List<UniqueDeviceDto> getOnline() {
        return uniqueToUniqueForView(getOnlineDevices());
    }

    public List<UniqueDeviceDto> getOffline() {
        return uniqueToUniqueForView(getOfflineDevices());
    }

    public List<UniqueDeviceDto> getAllDevice() {
        List<Device> devices = new ArrayList<>();
        devices.addAll(getOnlineDevices());
        devices.addAll(getOfflineDevices());
        return uniqueToUniqueForView(devices);
    }

    @PostConstruct
    private void init() {
        offlineDevices = deviceRepository.findAll();
    }
}
