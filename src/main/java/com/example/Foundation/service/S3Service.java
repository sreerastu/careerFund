package com.example.Foundation.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.BucketName}")
    private String bucketName;

    public void uploadImageToS3(String folderName,String fileName ,MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(new PutObjectRequest(bucketName, folderName + "/" + fileName, file.getInputStream(), metadata));
    }

    public void deleteImageFromS3(String imageName) {
        amazonS3.deleteObject(bucketName, imageName);
    }

    public Resource loadImageAsResource(String imageName) throws IOException {
        S3Object object = amazonS3.getObject(bucketName, imageName);
        byte[] imageBytes = object.getObjectContent().readAllBytes();
        object.close();
        return new ByteArrayResource(imageBytes);
    }
}
