package com.clickmedia.vasilis.vivapayments.model;

import java.io.Serializable;

/**
 * Created by Vasilis Fouroulis on 26/6/2017.
 */

public class User implements Serializable {

    private String name;
    private String email;
    private String surname;
    private String fullname;
    private String phone;

    public User(String name, String email, String surname, String phone) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.phone = phone;
        this.fullname = name + " " + surname;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
