package com.home.ledclockservice.dao;

import com.home.ledclockservice.model.UniqueDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueDeviceRepository extends JpaRepository<UniqueDevice, Integer> {
    UniqueDevice findFirstByDeviceId(String deviceId);
}
