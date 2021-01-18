package com.linkink.backend.vendor.services;

import com.linkink.backend.data.build.VendorBuilder;
import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.data.repository.PostRepository;
import com.linkink.backend.data.repository.VendorRepository;
import com.linkink.backend.vendor.bucket.BucketName;
import com.linkink.backend.vendor.profileimagestore.ProfileImageFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_SVG;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final ProfileImageFileStore profileImageFileStore;

    @Autowired
    public PostService(PostRepository postRepository, ProfileImageFileStore profileImageFileStore) {
        this.postRepository = postRepository;
        this.profileImageFileStore = profileImageFileStore;
    }

    public List<Post> getPosts(){
        Iterable<Post> posts = postRepository.findAll();
        List<Post> postList = new ArrayList<>();
        posts.forEach(postList::add);
        return postList;
    }

    public List<Post> getPostsByVendorId(Long vendorId){
        Iterable<Post> posts = postRepository.findByVendorProfileId(vendorId);
        List<Post> postList = new ArrayList<>();
        posts.forEach(postList::add);
        return postList;
    }

    public Post findByPostId(Long postId){
        return this.getPostOrThrow(postId);
    }

    public Post addPost(Vendor vendor){
        Post createdPost = new Post(vendor);
        return postRepository.save(createdPost);
    }

    public void updatePost(Long postId, Post updatedPost) {
        Post originalPost=this.getPostOrThrow(postId);
        if(originalPost.getPostId()!= updatedPost.getPostId())
            throw new IllegalStateException(String.format("Post Id %s does not match the record in the Requested Body", postId));
        postRepository.save(updatedPost);
    }

    public Post addImages(long postId, Image createdImage) {
        Post post = getPostOrThrow(postId);
        post.getImages().add(createdImage);
        return postRepository.save(post);
    }

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(String.format("User post %s not found", postId)));
    }

    public void deletePost(Long postId) {
        String path = String.format("postImages/%s", postId);
        profileImageFileStore.removeImage(path);
        postRepository.deleteById(postId);
        //delete Image data and also from Vendor list
    }

    public void deleteByVendorId(Long vendorId) {
        Iterable<Post> posts = postRepository.findByVendorProfileId(vendorId);
        List<Long> postIdList=new ArrayList<>();
        posts.forEach(post -> postIdList.add(post.getPostId()));
        postIdList.forEach(this::deletePost);
    }
}
