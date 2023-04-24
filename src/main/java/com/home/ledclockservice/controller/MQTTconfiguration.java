package com.home.ledclockservice.controller;

import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTconfiguration {
    @Value("${mqtt.clientId}")
    private String clientId;
    @Value("${mqtt.server}")
    private String server;
    @Value("${mqtt.port}")
    private int port;
    @Value("${mqtt.login}")
    private String login;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.topic}")
    private String mqttTopic;

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(5);
        mqttConnectOptions.setUserName(login);
        mqttConnectOptions.setPassword(password.toCharArray());

        IMqttClient mqttClient = new MqttClient(server + ":" + port, clientId);
        mqttClient.setCallback(new MQTTcallbacks.CallBacks());
        mqttClient.connect(mqttConnectOptions);
        mqttClient.subscribe(mqttTopic);
        return mqttClient;
    }
}
