package com.linkink.backend.data.projections;

//https://www.baeldung.com/spring-data-jpa-projections

import org.springframework.beans.factory.annotation.Value;

public interface VendorView {
    long getProfileId();
    //@Value("#{target.FIRST_NAME + ' ' + target.FIRST_NAME}")
    String getFullName();
}
