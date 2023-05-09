package com.home.ledclockservice.view;

import com.home.ledclockservice.service.DAOServices;
import com.home.ledclockservice.model.DeviceForView;
import com.home.ledclockservice.service.DeviceServices;
import com.home.ledclockservice.model.UniqueDevice;
import com.home.ledclockservice.model.UniqueDeviceForView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebView {
    private final DAOServices daoServices;
    private final DeviceServices deviceServices;

    @Autowired
    public WebView(DAOServices daoServices, DeviceServices deviceServices) {
        this.daoServices = daoServices;
        this.deviceServices = deviceServices;
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
        model.addAttribute("time", getTime());
        model.addAttribute("device", getLastDeviceInfo(deviceId));
        model.addAttribute("messages", get5LastMessage(deviceId));
        model.addAttribute("location", "DEVICE");
        model.addAttribute("parameters", null);
        return "device";
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
