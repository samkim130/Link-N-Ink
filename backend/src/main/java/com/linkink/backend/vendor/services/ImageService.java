package com.linkink.backend.vendor.services;

import com.linkink.backend.data.entity.Image;
import com.linkink.backend.data.entity.Post;
import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.data.repository.ImageRepository;
import com.linkink.backend.vendor.bucket.BucketName;
import com.linkink.backend.vendor.profileimagestore.ProfileImageFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProfileImageFileStore profileImageFileStore;

    @Autowired
    public ImageService(ImageRepository imageRepository,ProfileImageFileStore profileImageFileStore) {
        this.imageRepository = imageRepository;
        this.profileImageFileStore = profileImageFileStore;
    }
    public List<Image> getImages(){
        Iterable<Image> images = imageRepository.findAll();
        List<Image> imageList = new ArrayList<>();
        images.forEach(imageList::add);
        return imageList;
    }

    //since the method throws runtime exceptions, the method doesn't need to state "throws ....Exception"
    public Image addPostImage(Vendor vendor,Post post, MultipartFile file) {
        //1. check if image is not empty
        isFileEmpty(file);
        //2. if file is an image
        isImage(file);
        //3. grab metadata if any
        Map<String, String> metadata = extractMetadata(file);
        //4. create new Image entity
        Image image= new Image(vendor,post,null);
        imageRepository.save(image);

        //5. store image in s3 & update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/postImages/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(),
                post.getPostId(),
                image.getImageId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            profileImageFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            image.setPostImageLink(filename);
            imageRepository.save(image);
            return image;
        } catch ( IOException e){
            imageRepository.delete(image);
            throw new IllegalStateException(e);
        }

    }

    public byte[] downloadPostImage(Long imageId) {
        Image image = getImageOrThrow(imageId);

        String path = String.format("%s/postImages/%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                image.getPost().getPostId(),
                image.getImageId());

        return image.getPostImageLink()
                .map(key -> profileImageFileStore.download(path, key))
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

    private Image getImageOrThrow(Long imageId) {
        Image foundImage=imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalStateException(String.format("User post image %s not found", imageId)));
        return foundImage;
    }

    //@org.jetbrains.annotations.NotNull
    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    @Transactional
    public long deleteByPostId(Long postId) {
        return imageRepository.deleteByPostPostId(postId);
    }

    @Transactional
    public long deleteByVendorId(Long vendorId) {
        return imageRepository.deleteByVendorProfileId(vendorId);
    }
}
