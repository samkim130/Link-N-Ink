package com.linkink.backend.vendor.services;


import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.config.Encryption;
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

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final VendorService vendorService;
    private final ImageService imageService;
    private final Encryption encryption;

    @Autowired
    public PostController(PostService postService, VendorService vendorService, ImageService imageService,Encryption encryption) {
        this.postService = postService;
        this.vendorService=vendorService;
        this.imageService=imageService;
        this.encryption=encryption;
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

    @GetMapping(
            path="/byVendor/{vendorId}"
    )
    public ResponseEntity getPostsByVendorId(@PathVariable("vendorId") Long vendorId) {
        List<Post> postList = postService.getPostsByVendorId(vendorId);
        if (postList == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(postList);
        }
    }


    @PostMapping(
            path="/{vendorId}/add/wAdmin/{password}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity addPost(@PathVariable("password") String password,@PathVariable("vendorId") Long vendorId,
                                  @RequestParam("file")MultipartFile file) throws URISyntaxException {
        if(!encryption.checkAccess(password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Vendor postOwner = vendorService.findByProfileId(vendorId);
        Post createdPost= postService.addPost(postOwner);
        if (postOwner == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            Image createdImage=imageService.addPostImage(postOwner,createdPost, file);
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/image/{id}/download")
                    .buildAndExpand(createdImage.getImageId());
            List<Image> imageList = new ArrayList<>();
            //will be updated to accept multiple files
            imageList.add(createdImage);

            vendorService.addPostAndImages(vendorId,createdPost, imageList);
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

    @PutMapping(
            path="/{postId}/add/wAdmin/{password}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity addImagesToPost(@PathVariable("password") String password,@PathVariable("postId") Long postId,
                                  @RequestParam("file")MultipartFile file) throws URISyntaxException {
        if(!encryption.checkAccess(password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Post orgPost = postService.findByPostId(postId);
        Vendor vendor = orgPost.getVendor();
        if (orgPost == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            Image createdImage=imageService.addPostImage(vendor,orgPost, file);
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/image/{id}/download")
                    .buildAndExpand(createdImage.getImageId());
            List<Image> imageList = new ArrayList<>();
            //will be updated to accept multiple files
            imageList.add(createdImage);

            vendorService.addImages(vendor.getProfileId(), imageList);
            orgPost=postService.addImages(orgPost.getPostId(),createdImage);

            return ResponseEntity.ok(orgPost);
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

    @DeleteMapping(
            path="/{postId}/remove/wAdmin/{password}"
    )
    public ResponseEntity removeVendor(@PathVariable("password") String password,@PathVariable("postId") Long postId){
        if(!encryption.checkAccess(password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        long removedImages=imageService.deleteByPostId(postId);
        postService.deletePost(postId);
        return ResponseEntity.ok(removedImages);
    }

}
