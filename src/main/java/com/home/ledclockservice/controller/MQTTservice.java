package com.home.ledclockservice.controller;

import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQTTservice {
    private final IMqttClient mqttClient;

    @Autowired
    public MQTTservice(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
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
