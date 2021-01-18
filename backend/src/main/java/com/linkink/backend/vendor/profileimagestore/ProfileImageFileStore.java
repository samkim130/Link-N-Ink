package com.linkink.backend.vendor.profileimagestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.linkink.backend.vendor.bucket.BucketName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileImageFileStore {
    private final AmazonS3 s3;

    @Autowired
    public ProfileImageFileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public AmazonS3 getS3() {
        return s3;
    }

    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream){
        //create a new ObjectMetadata here
        //https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingMetadata.html
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //if optional map data is valid, then accept and run the code inside ifPresent()
        optionalMetaData.ifPresent(map->{
            //check if map is not empty, this is different from checking if the optional metadata value had been passed as a parameter
            if(!map.isEmpty()) {
                map.forEach((key,value)->{
                    switch (key){
                        case "Content-Type": objectMetadata.setContentType(value);
                            break;
                        case "Content-Length":  objectMetadata.setContentLength(Long.parseLong(value));
                            break;
                        default: break;
                    }
                });
                //mapping the optional metadata into the ObjectMetadata just created
                //map.forEach((key,value)->objectMetadata.addUserMetadata(key,value));
                //map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try{
            s3.putObject(path, fileName,inputStream, objectMetadata);
        } catch(AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }

    }

    public byte[] download(String path, String key){
        try{
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch(AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download file from s3", e);
        }
    }

    public void removeImage(String path){

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(BucketName.PROFILE_IMAGE.getBucketName())
                        .withPrefix(path + "/");

        ObjectListing objects = s3.listObjects(listObjectsRequest);
        for (;;) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.size() < 1) {
                break;
            }
            summaries.forEach(s ->{
                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BucketName.PROFILE_IMAGE.getBucketName(), s.getKey());
               s3.deleteObject(deleteObjectRequest);
            });
            objects = s3.listNextBatchOfObjects(objects);
        }
    }

}
