package com.linkink.backend.vendor.profileimagestore;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.linkink.backend.vendor.services.VendorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class ProfileImageStoreTests {
    @Autowired
    private VendorService vendorService;

    @Test
    public void testAWSConnection(){
        ProfileImageFileStore fileStore = vendorService.getProfileImageFileStore();
        AmazonS3 s3 = fileStore.getS3();
        List<Bucket> buckets = s3.listBuckets();
        System.out.println(buckets);
    }
}
