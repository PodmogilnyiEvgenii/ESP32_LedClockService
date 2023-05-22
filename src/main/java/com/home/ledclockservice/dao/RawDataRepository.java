package com.home.ledclockservice.dao;

import com.home.ledclockservice.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Integer> {
    RawData findFirstByDeviceIdOrderByIdDesc(String deviceId);
    List<RawData> findTop5ByDeviceIdOrderByIdDesc (String deviceId);
    RawData findFirstById(int dataId);
}
