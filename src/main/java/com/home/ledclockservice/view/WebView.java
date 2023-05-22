package com.home.ledclockservice.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.home.ledclockservice.dto.DeviceDto;
import com.home.ledclockservice.dto.DeviceOptions;
import com.home.ledclockservice.mqtt.MQTTConfiguration;
import com.home.ledclockservice.mqtt.MQTTController;
import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.service.DeviceServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class WebView {
    private final DAOServices daoServices;
    private final DeviceServices deviceServices;
    private final MQTTController mqttController;
    private final MQTTConfiguration mqttConfiguration;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("location", "MAIN");
        return "index";
    }

    @RequestMapping(value = {"/online"}, method = RequestMethod.GET)
    public String online(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("devices", deviceServices.getOnline());
        model.addAttribute("location", "ONLINE");
        return "all";
    }

    @RequestMapping(value = {"/offline"}, method = RequestMethod.GET)
    public String offline(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("devices", deviceServices.getOffline());
        model.addAttribute("location", "OFFLINE");
        return "all";
    }

    @RequestMapping(value = {"/all"}, method = RequestMethod.GET)
    public String all(Model model) {
        model.addAttribute("time", getTime());
        model.addAttribute("devices", deviceServices.getAllDevice());
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
            deviceServices.setDeviceOptions(null);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode =objectMapper.createObjectNode();
            objectNode.put("id", deviceId);
            objectNode.put("type", "get");
            mqttController.publish(mqttConfiguration.getMqttTopic(), objectNode.toString(), 2, false);

            do {
                Thread.sleep(100);
            } while (deviceServices.getDeviceOptions() == null);

        } catch (MqttException | InterruptedException e) {
            log.error("MQTT get message fail: {}", e.getMessage());
        }
        DeviceOptions deviceOptions = deviceServices.getDeviceOptions();
        model.addAttribute("time", getTime());
        model.addAttribute("rawData", getLastDeviceInfo(deviceId));
        model.addAttribute("messages", get5LastMessage(deviceId));
        model.addAttribute("location", "DEVICE");
        model.addAttribute("saveParameters", deviceOptions);
        return "device";
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("saveParameters") DeviceOptions saveDeviceOptions, Model model/*, BindingResult result*/) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            saveDeviceOptions.setType("set");
            String mqttSetMessage = objectMapper.writeValueAsString(saveDeviceOptions);
            mqttController.publish(mqttConfiguration.getMqttTopic(), mqttSetMessage, 2, false);
        } catch (JsonProcessingException | MqttException e) {
            log.error("MQTT set message fail: {}", e.getMessage());
        }
        model.addAttribute("time", getTime());
        model.addAttribute("location", "SAVE");
        model.addAttribute("deviceId", saveDeviceOptions.getId());
        return "save";
    }

    private DeviceDto getLastDeviceInfo(String deviceId) {
        return new DeviceDto(daoServices.findLastDevice(deviceId));
    }

    private List<String> get5LastMessage(String deviceId) {
        return daoServices.find5LastMessages(deviceId);
    }

    private String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
