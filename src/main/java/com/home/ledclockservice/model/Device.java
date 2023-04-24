package com.home.ledclockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {

    private String id;
    private String name;
    private String lastMessage;


}
