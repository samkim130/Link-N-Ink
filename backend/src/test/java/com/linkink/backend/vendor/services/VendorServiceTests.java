package com.linkink.backend.vendor.services;


import com.linkink.backend.data.entity.Vendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class VendorServiceTests {

    @Autowired
    private VendorService vendorService;

    @Test
    public void testVendorServices(){
        Vendor vendor_A= new Vendor("abc", "ABC", "company", "whatever@com", "AB Street", "USA", "NJ","999-999-9999", null);
        Vendor vendor_B= new Vendor("cdf", "CDF", "company", "whatever@com", "AB Street", "USA", "NJ","999-999-9999", null);
        Vendor vendor_C= new Vendor("efg", "EFG", "company", "whatever@com", "AB Street", "USA", "NJ","999-999-9999", null);
        Vendor vendor_D= new Vendor("hij", "HIJ", "company", "whatever@com", "AB Street", "USA", "NJ","999-999-9999", null);

        vendorService.addVendor(vendor_A);
        vendorService.addVendor(vendor_B);
        vendorService.addVendor(vendor_C);

        vendor_D= vendorService.addVendor(vendor_D);
        vendor_D.setCompany("updated Company");
        vendor_D.setAddress("new CD Street");
        vendor_D.setEmailAddress("newEmail@whatever.com");

        vendorService.updateVendorInformation(vendor_D.getProfileId(), vendor_D);

        List<Vendor> vendorList = vendorService.getVendors();

        System.out.println(vendorList);

    }

}

