package com.home.ledclockservice.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.ledclockservice.controller.MQTTController;
import com.home.ledclockservice.model.DeviceOptions;
import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.model.DeviceForView;
import com.home.ledclockservice.service.DeviceServices;
import com.home.ledclockservice.model.UniqueDevice;
import com.home.ledclockservice.model.UniqueDeviceForView;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Controller
public class WebView {
    private final DAOServices daoServices;
    private final DeviceServices deviceServices;
    private final MQTTController mqttController;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    @Autowired
    public WebView(DAOServices daoServices, DeviceServices deviceServices, MQTTController mqttController) {
        this.daoServices = daoServices;
        this.deviceServices = deviceServices;
        this.mqttController = mqttController;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("location", "MAIN");
        return "index";
    }

    @RequestMapping(value = {"/online"}, method = RequestMethod.GET)
    public String online(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("exdevices", getOnlineDevice());
        model.addAttribute("location", "ONLINE");
        return "all";
    }

    @RequestMapping(value = {"/offline"}, method = RequestMethod.GET)
    public String offline(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("exdevices", getOfflineDevice());
        model.addAttribute("location", "OFFLINE");
        return "all";
    }

    @RequestMapping(value = {"/all"}, method = RequestMethod.GET)
    public String all(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("exdevices", getAllDevice());
        model.addAttribute("location", "ALL");
        return "all";
    }

    @RequestMapping(value = {"/help"}, method = RequestMethod.GET)
    public String help(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("location", "HELP");
        return "help";
    }

    @RequestMapping(value = {"/{deviceId}"}, method = RequestMethod.GET)
    public String device(@PathVariable("deviceId") String deviceId, Model model) {
        try {
            mqttController.publish(mqttTopic, "{\"id\":\"" + deviceId + "\",\"type\":\"get\"}", 2, false);
            do {
                Thread.sleep(100);
            } while (deviceServices.getDevicesOptions().stream().map(s -> s.getDeviceId().equals(deviceId)).findFirst().isEmpty());
        } catch (MqttException | InterruptedException e) {
            log.error("MQTT get message fail: {}", e.getMessage());
            //throw new RuntimeException(e);
        }
        DeviceOptions deviceOptions = deviceServices.getDevicesOptions().stream().findFirst().get();
        deviceServices.getDevicesOptions().remove(deviceOptions);
        model.addAttribute("time", getTime());
        model.addAttribute("device", getLastDeviceInfo(deviceId));
        model.addAttribute("messages", get5LastMessage(deviceId));
        model.addAttribute("location", "DEVICE");
        model.addAttribute("saveParameters", /*new DeviceOptions()*/deviceOptions);
        return "device";
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("saveParameters") DeviceOptions saveDeviceOptions, Model model/*, BindingResult result*/) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            saveDeviceOptions.setType("set");
            String mqttSetMessage = objectMapper.writeValueAsString(saveDeviceOptions);
            mqttController.publish(mqttTopic, mqttSetMessage, 2, false);
        } catch (JsonProcessingException | MqttException e) {
            log.error("MQTT set message fail: {}", e.getMessage());
            //throw new RuntimeException(e);
        }
        model.addAttribute("time", getTime());
        model.addAttribute("location", "SAVE");
        model.addAttribute("deviceId", saveDeviceOptions.getDeviceId());
        return "save";
    }

    private List<UniqueDeviceForView> getOnlineDevice() {
        return deviceServices.uniqueToUniqueForView(deviceServices.getOnlineDevices());
    }

    private List<UniqueDeviceForView> getOfflineDevice() {
        return deviceServices.uniqueToUniqueForView(deviceServices.getOfflineDevices());
    }

    private List<UniqueDeviceForView> getAllDevice() {
        List<UniqueDevice> uniqueDevices = new ArrayList<>();
        uniqueDevices.addAll(deviceServices.getOnlineDevices());
        uniqueDevices.addAll(deviceServices.getOfflineDevices());
        return deviceServices.uniqueToUniqueForView(uniqueDevices);
    }

    private DeviceForView getLastDeviceInfo(String deviceId) {
        return new DeviceForView(daoServices.findLastDevice(deviceId));
    }

    private List<String> get5LastMessage(String deviceId) {
        return daoServices.find5LastMessages(deviceId);
    }

    private String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
