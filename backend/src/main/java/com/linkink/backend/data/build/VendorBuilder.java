package com.linkink.backend.data.build;

import com.linkink.backend.data.entity.Vendor;

import java.util.Optional;

public class VendorBuilder {
    private String firstName;
    private String lastName;
    private String company;
    private String emailAddress;
    private String address;
    private String country;
    private String city;
    private String state;
    private String phoneNumber;
    private String profileLink;

    public VendorBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public VendorBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public VendorBuilder setCompany(String company) {
        this.company = company;
        return this;
    }

    public VendorBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public VendorBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public VendorBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public VendorBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public VendorBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public VendorBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public VendorBuilder setProfileLink(Optional<String> profileLink) {
        if(profileLink.isPresent())
            this.profileLink=profileLink.get();
        return this;
    }

    public Vendor buildVendor(){
        return new Vendor(firstName,lastName,company,emailAddress,address,city,country,state,phoneNumber,profileLink);
    }
}
