package com.puulapp.ethio_cargo.spacecraft;

public class NotSpacecraft {
    public String notification, not_time;

    public NotSpacecraft(String notification, String not_time) {
        this.notification = notification;
        this.not_time = not_time;

    }

    public String getNot_time() {
        return not_time;
    }

    public void setNot_time(String not_time) {
        this.not_time = not_time;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
