package com.look.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ivan
 * Date: 25.02.16
 * Time: 19:12
 */
public class Company {

    private int id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;

    private List<Beneficial> beneficials;

    public Company() {}

    public Company(int id, String name, String address, String city, String country) {
        this(id, name, address, city, country, "", "");
    }

    public Company(int id, String name, String address, String city, String country, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Company(String name, String address, String city, String country, String email, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Beneficial> getBeneficials() {
        return beneficials;
    }

    public void setBeneficials(List<Beneficial> beneficials) {
        this.beneficials = beneficials;
    }
}

