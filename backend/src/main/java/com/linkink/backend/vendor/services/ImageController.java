package com.linkink.backend.vendor.services;

import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity getImages() {
        List<Image> imageList = imageService.getImages();
        if (imageList == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(imageList);
        }
    }

    @GetMapping(
            path="/{imageId}/image/download"
    )
    public ResponseEntity downloadPostImage(@PathVariable("imageId")Long imageId) throws URISyntaxException {
        return ResponseEntity.ok(imageService.downloadPostImage(imageId));
    }
}
