package com.linkink.backend.vendor.services;

import com.linkink.backend.data.build.VendorBuilder;
import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.data.repository.ImageRepository;
import com.linkink.backend.data.repository.PostRepository;
import com.linkink.backend.data.repository.VendorRepository;
import com.linkink.backend.vendor.bucket.BucketName;
import com.linkink.backend.vendor.profileimagestore.ProfileImageFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

// this is for IMAGE_* in method uploadUserProfileImage
import static org.apache.http.entity.ContentType.*;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;
    private final ProfileImageFileStore profileImageFileStore;

    @Autowired
    public VendorService(VendorRepository vendorRepository,ProfileImageFileStore profileImageFileStore) {
        this.vendorRepository = vendorRepository;
        this.profileImageFileStore = profileImageFileStore;
    }

    public VendorRepository getVendorRepository() {
        return vendorRepository;
    }

    public ProfileImageFileStore getProfileImageFileStore() {
        return profileImageFileStore;
    }

    public List<Vendor> getVendors(){
        Iterable<Vendor> vendors = vendorRepository.findAll();
        List<Vendor> vendorList = new ArrayList<>();
        vendors.forEach(vendorList::add);
        return vendorList;
    }

    public Vendor findByProfileId(Long vendorId){
        return this.getVendorOrThrow(vendorId);
    }

    public Vendor addVendor(Vendor newVendor){
        Vendor createdVendor = new VendorBuilder()
                .setFirstName(newVendor.getFirstName())
                .setLastName(newVendor.getLastName())
                .setCompany(newVendor.getCompany())
                .setEmailAddress(newVendor.getEmailAddress())
                .setAddress(newVendor.getAddress())
                .setCountry(newVendor.getCountry())
                .setState(newVendor.getState())
                .setPhoneNumber(newVendor.getPhoneNumber())
                .buildVendor();
        return vendorRepository.save(createdVendor);
    }

    public Vendor updateVendorInformation(Long vendorId, Vendor updatedVendor) {
        Vendor originalVendor=this.getVendorOrThrow(vendorId);
        if(originalVendor.getProfileId()!= updatedVendor.getProfileId())
            throw new IllegalStateException(String.format("Vendor Id %s does not match the record in the Requested Body", vendorId));
        return vendorRepository.save(updatedVendor);
    }

    public void deleteVendor(Long vendorId) {
        String path = String.format("profileImages/%s", vendorId);
        profileImageFileStore.removeProfile(path);
        vendorRepository.deleteById(vendorId);

    }

    public Vendor addPostAndImages(Long vendorId, Post createdPost, List<Image> imageList) {
        Vendor vendor = getVendorOrThrow(vendorId);
        vendor.getPosts().add(createdPost);
        imageList.forEach(vendor.getImages()::add);
        return vendorRepository.save(vendor);
    }

    //since the method throws runtime exceptions, the method doesn't need to state "throws ....Exception"
    public void uploadVendorProfileImage(Long vendorId, MultipartFile file) {
        //1. check if image is not empty
        isFileEmpty(file);
        //2. if file is an image
        isImage(file);
        //3. check if user exists
        Vendor vendor = getVendorOrThrow(vendorId);
        //4. grab metadata if any
        Map<String, String> metadata = extractMetadata(file);

        //5. store image in s3 & update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/profileImages/%s", BucketName.PROFILE_IMAGE.getBucketName(),vendor.getProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            profileImageFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            vendor.setProfileLink(filename);
            vendorRepository.save(vendor);

            //6. mechanism to keep track of how many files in the file directory and keep the most recent 3
        } catch ( IOException e){
            throw new IllegalStateException(e);
        }

    }

    public byte[] downloadVendorProfileImage(Long vendorId, String imageLink) {
        Vendor vendor = getVendorOrThrow(vendorId);

        String path = String.format("%s/profileImages/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                vendor.getProfileId());

        return vendor.getProfileLink()
                .map(key -> {
                    if (key.compareTo(imageLink)!=0)
                        throw new IllegalStateException("Image Link does not match");
                    return profileImageFileStore.download(path, key);
                })
                .orElse(new byte[0]);
    }

    private void isImage(MultipartFile file) {
        //if(!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF, IMAGE_BMP, IMAGE_SVG).contains(file.getContentType()))
        //"File must be an image [image/png]"
        if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_BMP.getMimeType(), IMAGE_SVG.getMimeType()).contains(file.getContentType()))
            throw new IllegalStateException("File must be an image ["+ file.getContentType()+"]\nExpected Types: ["+IMAGE_JPEG+" "+IMAGE_PNG+" "+IMAGE_GIF+" "+IMAGE_BMP+" "+IMAGE_SVG+"]");
    }

    private void isFileEmpty(MultipartFile file) {
        if(file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file [ "+ file.getSize()+"]");
    }

    private Vendor getVendorOrThrow(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", vendorId)));
    }

    //@org.jetbrains.annotations.NotNull
    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

}
