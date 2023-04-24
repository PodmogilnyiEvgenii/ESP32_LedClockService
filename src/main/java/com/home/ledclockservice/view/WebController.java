package com.home.ledclockservice.view;

import com.home.ledclockservice.AppContext;
import com.home.ledclockservice.model.Devices;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class WebController {

    //@Value("${welcome.message}")
    private String message;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("time", LocalDateTime.now().toLocalTime().toString());
        model.addAttribute("devices", AppContext.getAppContext().getBean("devices", Devices.class).getDevices());
        return "index";
    }
}
