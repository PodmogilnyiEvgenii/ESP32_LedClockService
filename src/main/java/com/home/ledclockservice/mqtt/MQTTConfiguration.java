package com.home.ledclockservice.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MQTTConfiguration {
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
}
