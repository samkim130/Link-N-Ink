package com.linkink.backend.vendor.services;

import com.linkink.backend.data.entity.Vendor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1/vendor")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity getVendors() {
        List<Vendor> vendorList = vendorService.getVendors();
        if (vendorList == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(vendorList);
        }
    }

    @GetMapping(
            path="/{vendorId}/get"
    )
    public ResponseEntity getVendor(@PathVariable("vendorId") Long vendorId){
        Vendor foundVendor= vendorService.findByProfileId(vendorId);
        if (foundVendor == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundVendor);
        }
    }

    @PostMapping(
            path="/add"
    )
    public ResponseEntity addVendor(@RequestBody Vendor newVendor) throws URISyntaxException {
        Long createdId= vendorService.addVendor(newVendor).getProfileId();
        if (createdId == null) {
            //some kind of sql error catch leading here
            return ResponseEntity.notFound().build();
        } else {
                URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/vendor/{id}")
                    .buildAndExpand(createdId)
                    .toUri();

            //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
            return ResponseEntity.created(uri)
                    .body(createdId);
        }
    }

    @PutMapping(
            path="/{vendorId}/update"
    )
    public ResponseEntity updateVendorInformation(@PathVariable("vendorId") Long vendorId, @RequestBody Vendor updatedVendor){
        Vendor savedUpdate = vendorService.updateVendorInformation(vendorId, updatedVendor);
        return ResponseEntity.ok(savedUpdate);
    }

    @DeleteMapping(
            path="/{vendorId}/remove"
    )
    public ResponseEntity removeVendor(@PathVariable("vendorId") Long vendorId){
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(
            path="/{vendorId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity uploadVendorProfileImage(@PathVariable("vendorId") Long vendorId,
                                         @RequestParam("file")MultipartFile file){
        vendorService.uploadVendorProfileImage(vendorId, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/vendor/{id}/image/download")
                .buildAndExpand(vendorId)
                .toUri();
        return ResponseEntity.created(uri)
                .build();
    }

    @GetMapping(
            path="/{vendorId}/image/download/{imageLink}"
    )
    public ResponseEntity downloadVendorProfileImage(@PathVariable("vendorId")Long vendorId,@PathVariable("imageLink")String imageLink) {
        return ResponseEntity.ok(vendorService.downloadVendorProfileImage(vendorId,imageLink));
    }
}
