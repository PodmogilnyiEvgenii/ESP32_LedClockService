package com.home.ledclockservice.dao;

import com.home.ledclockservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device findFirstByDeviceIdOrderByDataIdDesc(String deviceId);
    List<Device> findTop5ByDeviceIdOrderByDataIdDesc (String deviceId);
    Device findFirstByDataId(int dataId);
}
