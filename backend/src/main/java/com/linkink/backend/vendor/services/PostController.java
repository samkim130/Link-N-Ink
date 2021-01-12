package com.linkink.backend.vendor.services;


import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

class PostForm {
    private Long vendorId;
    private MultipartFile file;

    public Long getVendorId() {
        return vendorId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final VendorService vendorService;
    private final ImageService imageService;

    @Autowired
    public PostController(PostService postService, VendorService vendorService, ImageService imageService) {
        this.postService = postService;
        this.vendorService=vendorService;
        this.imageService=imageService;
    }

    @GetMapping
    public ResponseEntity getPosts() {
        List<Post> postList = postService.getPosts();
        if (postList == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(postList);
        }
    }


    @PostMapping(
            path="/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity addPost(@RequestBody PostForm postForm) throws URISyntaxException {
        Vendor postOwner = vendorService.findByProfileId(postForm.getVendorId());
        Post createdPost= postService.addPost(postOwner);
        if (postOwner == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            Image createdImage=imageService.addPostImage(postOwner,createdPost, postForm.getFile());
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/image/{id}/image/download")
                    .buildAndExpand(createdImage.getImageId());
            List<Image> imageList = new ArrayList<>();
            //will be updated to accept multiple files
            imageList.add(createdImage);

            vendorService.addPostAndImages(postForm.getVendorId(),createdPost, imageList);
            createdPost=postService.addImages(createdPost.getPostId(),createdImage);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/post/{id}")
                    .buildAndExpand(createdPost.getPostId())
                    .toUri();

            //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
            return ResponseEntity.created(uri)
                    .body(createdPost);
        }
    }

    @GetMapping(
            path="/{postId}"
    )
    public ResponseEntity getPost(@PathVariable("postId") Long postId){
        Post foundPost= postService.findByPostId(postId);
        if (foundPost == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundPost);
        }
    }
}
