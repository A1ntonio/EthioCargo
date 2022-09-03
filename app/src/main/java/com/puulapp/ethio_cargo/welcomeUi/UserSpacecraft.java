package com.puulapp.ethio_cargo.welcomeUi;

public class UserSpacecraft {
    public String u_name, u_email;

    public UserSpacecraft(String u_name, String u_email) {
        this.u_name = u_name;
        this.u_email = u_email;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }
}
