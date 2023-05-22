package com.home.ledclockservice.scheduled;

import com.home.ledclockservice.service.DeviceServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusChecker {
    private final DeviceServices deviceServices;

    @Scheduled(fixedDelay = 60000)
    void check() {
        deviceServices.checkDeviceStatus();
    }
}
