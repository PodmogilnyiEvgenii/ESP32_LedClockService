package com.home.ledclockservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "clock", name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private int id;
    @Column
    private String deviceId;
    @Column
    private String name;
    @Column
    private int lastDataId;
    @Transient
    private Status status=new Status();
}
