package com.linkink.backend.vendor.bucket;

public enum BucketName {
    PROFILE_IMAGE("vendor-images-linkink-app");

    private final String bucketName;

    BucketName(String bucketName){
        this.bucketName=bucketName;
    }

    public String getBucketName(){
        return bucketName;
    }
}
