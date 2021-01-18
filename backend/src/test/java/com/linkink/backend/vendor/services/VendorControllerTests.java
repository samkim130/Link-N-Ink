package com.linkink.backend.vendor.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.linkink.backend.data.entity.Vendor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendorControllerTests {
    @Autowired
    VendorController vendorController;

    private final HttpServletRequest mockRequest = new MockHttpServletRequest();

    @Before
    public void before() {
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    public void integrationTestExample() throws URISyntaxException {

        Vendor vendor_A= new Vendor("abc", "ABC", "company", "whatever@com", "AB Street", "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_B= new Vendor("cdf", "CDF", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_C= new Vendor("efg", "EFG", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_D= new Vendor("hij", "HIJ", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        /*
        ResponseEntity RE_A=  vendorController.addVendor(vendor_A);
        ResponseEntity RE_B=  vendorController.addVendor(vendor_B);
        ResponseEntity RE_C=  vendorController.addVendor(vendor_C);
        ResponseEntity RE_D=  vendorController.addVendor(vendor_D);

        //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html
        assertThat(RE_A.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(RE_B.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(RE_C.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(RE_D.getStatusCode(), is(equalTo(HttpStatus.CREATED)));

        System.out.println(RE_A.getHeaders());
        */

    }



}
