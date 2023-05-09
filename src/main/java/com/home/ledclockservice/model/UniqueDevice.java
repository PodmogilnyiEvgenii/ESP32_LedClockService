package com.home.ledclockservice.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(schema = "clock", name = "devices")
public class UniqueDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private int id;
    @Column(name = "deviceid")
    private String deviceId;
    @Column
    private String name;
    @Column(name = "lastdataid")
    private int lastDataId;
    @JsonIgnore
    @Transient
    boolean isOnline = false;
    @JsonIgnore
    @Transient
    Timer timer;

    @PostConstruct
    public void createTimer() {
        timer=new Timer();
        isOnline = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isOnline = false;
                this.cancel();
            }
        }, TimeUnit.MINUTES.toMillis(2));
    }
}
