package com.home.ledclockservice.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.ledclockservice.dto.DeviceDataType;
import com.home.ledclockservice.dto.DeviceOptions;
import com.home.ledclockservice.model.Device;
import com.home.ledclockservice.model.RawData;
import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.service.DeviceServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class MQTTMessageParser {
    private final DeviceServices deviceServices;
    private final DAOServices daoServices;

    public void parseMessage(String mqttMessage) {
        MQTTTypes messageType = getTypeMessage(mqttMessage);
        if (messageType != null)
            switch (messageType) {
                case DATA:
                    RawData rawData = getDeviceFromJson(mqttMessage);
                    if (rawData != null) {
                        daoServices.saveDevice(rawData);
                        Device device = daoServices.UpdateUniqueDevice(rawData);
                        deviceServices.makeDeviceOnline(device);
                    }
                    break;

                case SET:
                    //System.out.println("set message");
                    break;

                case GET:
                    //System.out.println("get message");
                    break;

                case OPTIONS:
                    deviceServices.saveDeviceOptions(getDeviceOptionsFromJson(mqttMessage));
                    break;

                case ERROR:
                    break;
            }
    }

    private MQTTTypes getTypeMessage(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DeviceDataType deviceDataType = objectMapper.readValue(jsonString, DeviceDataType.class);
            if (deviceDataType.getId() == null || deviceDataType.getName() == null || deviceDataType.getType() == null) {
                throw new IllegalArgumentException();
            }
            return Arrays.stream(MQTTTypes.values()).filter(s -> s.getTitle().equals(deviceDataType.getType())).findFirst().orElse(MQTTTypes.ERROR);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            log.debug("Wrong message type: {}", e.toString());
            return null;
        }
    }

    private RawData getDeviceFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RawData rawData = objectMapper.readValue(jsonString, RawData.class);
            rawData.setLastMessage(jsonString);
            return rawData;
        } catch (JsonProcessingException e) {
            log.debug("Device parse from JSON error: {}", e.toString());
            return null;
        }
    }

    private DeviceOptions getDeviceOptionsFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, DeviceOptions.class);
        } catch (JsonProcessingException e) {
            log.debug("Device parse from JSON error: {}", e.toString());
            return null;
        }
    }
}
