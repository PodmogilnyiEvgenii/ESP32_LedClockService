package com.home.ledclockservice.controller;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTcallbacks {
    public static class CallBacks implements MqttCallback {
        @Override
        public void connectionLost(Throwable throwable) {
            System.out.println("Connection lost");
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            System.out.println("Get message: " + mqttMessage.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            try {
                System.out.println("Delivery message complete: " + iMqttDeliveryToken.getMessage());
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
