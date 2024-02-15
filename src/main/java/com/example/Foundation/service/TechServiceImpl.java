package com.example.Foundation.service;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.repositories.TechnologiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TechServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Value("${aws.s3.TechnologiesFolder}")
    private String folderName;
    @Autowired
    private TechnologiesRepository technologiesRepository;

    public Technologies saveTechnology(Technologies technologies, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            technologies.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return technologiesRepository.save(technologies);
    }

    public List<Technologies> getAllTechnologies() {
        List<Technologies> all = technologiesRepository.findAll();
        return all;
    }

    public Technologies updateTechnology(int techId, Technologies technologies, MultipartFile file) throws IOException {
        Technologies optionalArticle = technologiesRepository.findById(techId).orElseThrow(()->new RuntimeException("Technologies not found"));

        if (technologies.getTechTitle() != null) {
            optionalArticle.setTechTitle(technologies.getTechTitle());
        }
        if (technologies.getDescription() != null) {
            optionalArticle.setDescription(technologies.getDescription());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = technologies.getImage();
            String newImageName = file.getOriginalFilename();
            optionalArticle.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return technologiesRepository.save(optionalArticle);
    }

    public Technologies getTechnologyById(int techId) {
        Technologies tech = technologiesRepository.findById(techId).orElseThrow(() -> new RuntimeException("technology not found"));
        return tech;
    }
}
