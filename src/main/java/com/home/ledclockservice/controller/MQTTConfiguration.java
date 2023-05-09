package com.home.ledclockservice.controller;

import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.service.DeviceServices;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MQTTConfiguration {
    private final DeviceServices deviceServices;
    private final DAOServices daoServices;
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

    @Autowired
    public MQTTConfiguration(DeviceServices deviceServices, DAOServices daoServices) {
        this.deviceServices = deviceServices;
        this.daoServices = daoServices;
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(/*true*/false);
        mqttConnectOptions.setConnectionTimeout(5);
        mqttConnectOptions.setUserName(login);
        mqttConnectOptions.setPassword(password.toCharArray());
        return mqttConnectOptions;
    }

    @Bean
    public MqttAsyncClient mqttClient() throws MqttException {
            MqttAsyncClient mqttClient = new MqttAsyncClient(server + ":" + port, clientId);
            mqttClient.setCallback(new MQTTController.CallBacks(mqttTopic));
            mqttClient.connect(mqttConnectOptions());
            return mqttClient;
    }


}
