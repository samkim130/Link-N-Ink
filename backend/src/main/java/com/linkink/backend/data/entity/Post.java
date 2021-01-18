package com.linkink.backend.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="POSTS")
public class Post {
    @Id
    @Column(name="POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @JsonIgnoreProperties({"posts","images"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PROFILE_ID")
    private Vendor vendor;

    @Column(name="POST_DATE", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    private LocalDateTime postDate;
    @Column(name="LIKES")
    private int likes;

    @JsonIgnoreProperties({"vendor", "post"})
    @OneToMany(mappedBy="post",
            fetch=FetchType.LAZY,
            cascade= CascadeType.ALL,
            orphanRemoval=true)
    private Set<Image> images = new HashSet<>();

    protected Post() {
    }

    public Post(Vendor vendor) {
        this.vendor = vendor;
        this.likes = 0;
        images = new HashSet<>();
    }

    public long getPostId() {
        return postId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Set<Image> getImages() {
        return images;
    }
    public void setImages(Set<Image> images) {
        this.images=images;
    }

    public void incrementLikes() {
        this.likes+=1;
    }
    public void decrementLikes() {
        this.likes-=1;
    }
}
