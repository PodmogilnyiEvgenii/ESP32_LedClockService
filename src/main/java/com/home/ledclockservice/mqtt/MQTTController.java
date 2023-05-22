package com.home.ledclockservice.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQTTController {
    private static MqttAsyncClient mqttClient;
    private static MQTTConfiguration mqttConfiguration;
    private static MQTTMessageParser mqttMessageParser;

    @Autowired
    public MQTTController(MqttAsyncClient mqttClient, MQTTConfiguration mqttConfiguration, MQTTMessageParser mqttMessageParser) {
        MQTTController.mqttClient = mqttClient;
        MQTTController.mqttConfiguration = mqttConfiguration;
        MQTTController.mqttMessageParser = mqttMessageParser;
    }

    public void publish(String topic, String payload, int qos, boolean retained) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttClient.publish(topic, mqttMessage);
    }

    public static class MQTTCallBacks implements MqttCallbackExtended {

        @Override
        public void connectionLost(Throwable throwable) {
            log.warn("MQTT connection lost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) {
            log.info("MQTT get message: {}", mqttMessage);
            mqttMessageParser.parseMessage(mqttMessage.toString());

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
                mqttClient.subscribe(mqttConfiguration.getMqttTopic(), 0);
            } catch (MqttException e) {
                log.error("MQTT subscribe fail: {}", e.getMessage());
            }
        }
    }
}
