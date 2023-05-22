package com.home.ledclockservice.mqtt;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MQTTBeans {
    private final MQTTConfiguration mqttConfiguration;

    @Bean
    public MqttAsyncClient mqttClient() throws MqttException {
        MqttAsyncClient mqttClient = new MqttAsyncClient(
                mqttConfiguration.getServer() + ":" + mqttConfiguration.getPort(),
                mqttConfiguration.getClientId()
        );
        mqttClient.setCallback(new MQTTController.MQTTCallBacks());
        mqttClient.connect(mqttConnectOptions());
        return mqttClient;
    }

    private MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setConnectionTimeout(5);
        mqttConnectOptions.setUserName(mqttConfiguration.getLogin());
        mqttConnectOptions.setPassword(mqttConfiguration.getPassword().toCharArray());
        return mqttConnectOptions;
    }
}
