package com.linkink.backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


@Configuration
public class AmazonS3Config {
    @Value("${amazonProperties.accessKey}")
    private String keyId;

    @Value("${amazonProperties.secretKey}")
    private String key;

    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(keyId, key);

        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-east-2").build();
    }

}
/*
* @Bean
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
*/
