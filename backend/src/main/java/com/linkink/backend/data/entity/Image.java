package com.linkink.backend.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name="IMAGES")
public class Image {
    @Id
    @Column(name="IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @JsonIgnoreProperties({"images", "posts"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PROFILE_ID")
    private Vendor vendor;

    @JsonIgnoreProperties({"vendor", "images"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID")
    private Post post;

    @Column(name="POST_IMAGE_LINK")
    private String postImageLink;

    protected Image(){}

    public Image(Vendor vendor, Post post, String postImageLink) {
        this.vendor = vendor;
        this.post = post;
        this.postImageLink = postImageLink;
    }

    public long getImageId() {
        return imageId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Optional<String> getPostImageLink() {
        return Optional.ofNullable(postImageLink);
    }

    public void setPostImageLink(String postImageLink) {
        this.postImageLink = postImageLink;
    }
}

