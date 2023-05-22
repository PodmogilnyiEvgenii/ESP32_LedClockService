package com.home.ledclockservice.model;

import lombok.Data;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Data
public class Status {
    private Timer timer;
    private boolean isOnline;
   public void createOnlineTimer() {
        timer = new Timer();
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
