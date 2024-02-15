package com.example.Foundation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

@Configuration
public class AmazonS3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.BucketName}")
    private String bucketName;

    @Bean
    public AmazonS3 amazonS3Client() {
        // Create Amazon S3 client using instance profile credentials
        return AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(region)
                .build();
    }
}