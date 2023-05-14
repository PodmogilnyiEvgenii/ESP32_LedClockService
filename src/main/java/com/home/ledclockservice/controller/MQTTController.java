package com.home.ledclockservice.controller;

import com.home.ledclockservice.model.DeviceOptions;
import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.model.Device;
import com.home.ledclockservice.service.DeviceServices;
import com.home.ledclockservice.model.UniqueDevice;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQTTController {
    private static MqttAsyncClient  mqttClient;
    private static DeviceServices deviceServices;
    private static DAOServices daoServices;

    @Autowired
    public MQTTController(MqttAsyncClient mqttClient, DeviceServices deviceServices, DAOServices daoServices) {
        MQTTController.mqttClient = mqttClient;
        MQTTController.deviceServices = deviceServices;
        MQTTController.daoServices = daoServices;
    }

    public static class CallBacks implements MqttCallbackExtended {
        private final String mqttTopic;

        public CallBacks(String mqttTopic) {
            this.mqttTopic = mqttTopic;
        }

        @Override
        public void connectionLost(Throwable throwable) {
            log.warn("MQTT connection lost");
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            log.info("MQTT get message: {}", mqttMessage.toString());
            parseMessage(mqttMessage.toString());
        }

        private void parseMessage(String mqttMessage) {
            String messageType = deviceServices.getTypeMessage(mqttMessage);
            if (messageType != null)
                switch (messageType) {
                    case "data":
                        Device device = deviceServices.getDeviceFromJson(mqttMessage);
                        if (device != null) {
                            daoServices.saveDevice(device);
                            Device lastDevice = daoServices.findLastDevice(device.getDeviceId());   //last dataId
                            UniqueDevice uniqueDevice = daoServices.findUniqueDevice(device.getDeviceId());
                            if (uniqueDevice != null) {
                                uniqueDevice.setName(lastDevice.getName());
                                uniqueDevice.setLastDataId(lastDevice.getDataId());
                            } else {
                                uniqueDevice = new UniqueDevice();
                                uniqueDevice.setDeviceId(lastDevice.getDeviceId());
                                uniqueDevice.setName(lastDevice.getName());
                                uniqueDevice.setLastDataId(lastDevice.getDataId());
                            }
                            daoServices.saveUniqueDevice(uniqueDevice);

                            if (!deviceServices.updateDevice(deviceServices.getOnlineDevices(), uniqueDevice))
                                deviceServices.addDevice(deviceServices.getOnlineDevices(), uniqueDevice);

                            deviceServices.removeDevice(deviceServices.getOfflineDevices(), uniqueDevice);
                        }
                        break;

                    case "set":
                        //System.out.println("set message");
                        break;

                    case "get":
                        //System.out.println("get message");
                        break;

                    case "options":
                        deviceServices.saveDeviceOptions(deviceServices.getDeviceOptionsFromJson(mqttMessage));
                        break;
                }


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            log.info("MQTT delivery message complete");
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            try {
                log.info("MQTT connection complete");
                do {
                } while (!mqttClient.isConnected());
                mqttClient.subscribe(mqttTopic, 0);
            } catch (MqttException e) {
                log.error("MQTT subscribe fail: {}", e.getMessage());
            }
        }
    }

    public void publish(final String topic, final String payload, int qos, boolean retained) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttClient.publish(topic, mqttMessage);
    }

    @PreDestroy
    public void preDestroy() throws MqttException {
        mqttClient.disconnect();
        mqttClient.close();
    }
}
