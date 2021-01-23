package com.linkink.backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Configuration
public class MyConfig {

    @Bean
    @Profile("prod")
    public Encryption encryption(){
        ProdPropVarConfig prodPropVarConfig = new ProdPropVarConfig();
        return new Encryption(prodPropVarConfig.getAdminKey(),prodPropVarConfig.getAdminSalt());
    }

    @Bean
    @Profile("dev")
    public Encryption devEncryption() throws FileNotFoundException {
        String keys[]=readKey("./src/main/java/com/linkink/backend/config/admin.txt");
        return new Encryption(keys[0],keys[1]);
    }

    @Bean
    @Profile("prod")
    public AmazonS3 s3(){
        ProdPropVarConfig prodPropVarConfig = new ProdPropVarConfig();
        AWSCredentials awsCredentials = new BasicAWSCredentials(prodPropVarConfig.getKeyId(), prodPropVarConfig.getKey());

        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-east-2").build();
    }

    @Bean
    @Profile("dev")
    public AmazonS3 devS3() throws FileNotFoundException {
        String keys[]=readKey("./src/main/java/com/linkink/backend/config/cred.txt");
        AWSCredentials awsCredentials = new BasicAWSCredentials(keys[0],keys[1]);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-east-2")
                .build();
    }

    public String[] readKey(String filePath) throws FileNotFoundException {
        //file omitted through gitignore
        Scanner sc= new Scanner(new File(filePath));
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
