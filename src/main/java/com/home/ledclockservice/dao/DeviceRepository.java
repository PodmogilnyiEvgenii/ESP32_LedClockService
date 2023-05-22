package com.home.ledclockservice.dao;

import com.home.ledclockservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device findFirstByDeviceId(String deviceId);
}
