package com.home.ledclockservice.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MQTTCallBacks implements MqttCallbackExtended {
    private final MQTTMessageParser mqttMessageParser;

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
        log.info("MQTT connection complete");
    }
}
