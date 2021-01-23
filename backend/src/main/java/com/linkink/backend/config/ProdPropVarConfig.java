package com.linkink.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdPropVarConfig {
    //Amazon variables
    @Value("${amazonProperties.accessKey}")
    private String keyId;
    @Value("${amazonProperties.secretKey}")
    private String key;

    //Temp admin access variables
    @Value("${myAdminProperties.hashedPass}")
    private String adminKey;
    @Value("${myAdminProperties.salt}")
    private String adminSalt;

    public String getKeyId() {
        return keyId;
    }

    public String getKey() {
        return key;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public String getAdminSalt() {
        return adminSalt;
    }
}
