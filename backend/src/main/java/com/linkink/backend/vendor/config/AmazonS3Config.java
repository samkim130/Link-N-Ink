package com.linkink.backend.vendor.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

@Configuration
public class AmazonS3Config {
    @Bean
    public AmazonS3 s3() throws FileNotFoundException{
        String keys[]=readKey();
        AWSCredentials awsCredentials = new BasicAWSCredentials(keys[0],keys[1]);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-east-2")
                .build();
    }

    public String[] readKey() throws FileNotFoundException {
        //file omitted through gitignore
        Scanner sc= new Scanner(new File("./src/main/java/com/linkink/backend/vendor/config/cred.txt"));
        //fill in using this format
        //Scanner sc= new Scanner(new File("rootkey.csv"));
        String keys[] = new String[2];
        int i=0;
        while(sc.hasNext()){
            String key= sc.next();
            keys[i]=key.substring(key.indexOf("=")+1);
            i++;
        }
        sc.close();

        return keys;
    }

}
