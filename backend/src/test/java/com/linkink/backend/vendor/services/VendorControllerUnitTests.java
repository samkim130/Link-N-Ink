package com.linkink.backend.vendor.services;

import com.google.gson.Gson;
import com.linkink.backend.data.entity.Vendor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(VendorController.class)
public class VendorControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private VendorController vendorController;

    @MockBean
    private VendorService vendorService;

    private AutoCloseable closeable;

    @Before public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void unitTestExample() throws Exception {
        Vendor vendor_A= new Vendor("abc", "ABC", "company", "whatever@com", "AB Street", "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_B= new Vendor("cdf", "CDF", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_C= new Vendor("efg", "EFG", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);
        Vendor vendor_D= new Vendor("hij", "HIJ", "company", "whatever@com", "AB Street",  "Tenafly", "USA","NJ","999-999-9999", null);

        when(vendorService.addVendor(any(Vendor.class))).thenReturn(vendor_A);

        // simulate the form bean that would POST from the web page
        vendor_A.setEmailAddress("fredj@myemail.com");

        Gson gson = new Gson();
        String json_vendor_A = gson.toJson(vendor_A);

        // simulate the form submit (POST)
        mockMvc
                .perform(post("/api/v1/vendor/add")
                    .contentType(APPLICATION_JSON)
                    .content(json_vendor_A))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
