package com.example.flatemate;

public class HelperClass {
    public HelperClass() {
    }

    String name,contact,flat_no,vehicle,email,password;

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClass(String name, String contact, String flat_no, String vehicle, String email, String password) {
        this.name = name;
        this.contact = contact;
        this.flat_no = flat_no;
        this.vehicle = vehicle;
        this.email = email;
        this.password = password;
    }
}
