package com.home.ledclockservice.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQTTBeans {
    private final MQTTConfiguration mqttConfiguration;
    private final MQTTCallBacks mqttCallBacks;

    @Bean
    public MqttAsyncClient mqttClient() {
        try  {
            MqttAsyncClient mqttClient = new MqttAsyncClient(
                    mqttConfiguration.getServer() + ":" + mqttConfiguration.getPort(),
                    mqttConfiguration.getClientId());

            mqttClient.setCallback(mqttCallBacks);
            mqttClient.connect(mqttConnectOptions());

            do {
                Thread.sleep(100);
            } while (!mqttClient.isConnected());

            mqttClient.subscribe(mqttConfiguration.getMqttTopic(), 0);
            return mqttClient;

        } catch (MqttException | InterruptedException e) {
            log.error("MQTT create client fail: {}", e.getMessage());
        }
        return null;
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
