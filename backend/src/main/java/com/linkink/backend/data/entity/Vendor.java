package com.linkink.backend.data.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="VENDORS")
public class Vendor {
    @Id
    @Column(name="PROFILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;
    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="LAST_NAME")
    private String lastName;
    @Column(name="COMPANY")
    private String company;
    @Column(name="EMAIL_ADDRESS")
    private String emailAddress;
    @Column(name="ADDRESS")
    private String address;
    @Column(name="COUNTRY")
    private String country;
    @Column(name="STATE")
    private String state;
    @Column(name="PHONE_NUMBER")
    private String phoneNumber;
    @Column(name="PROFILE_IMAGE_LINK")
    private String profileLink;

    @OneToMany(mappedBy="vendor",
            fetch=FetchType.EAGER,
            cascade= CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy="vendor",
            fetch=FetchType.EAGER,
            cascade= CascadeType.ALL)
    @OrderColumn(name = "IMAGE_ID")
    private Set<Image> images = new HashSet<>();

    protected Vendor() {
    }

    public Vendor(String firstName, String lastName, String company, String emailAddress, String address, String country, String state, String phoneNumber, String profileLink) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.emailAddress = emailAddress;
        this.address = address;
        this.country = country;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.profileLink = profileLink;
        posts = new HashSet<>();
    }

    public long getProfileId() { return profileId; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<String> getProfileLink() {
        return Optional.ofNullable(profileLink);
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public Set<Post> getPosts() { return posts; }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
