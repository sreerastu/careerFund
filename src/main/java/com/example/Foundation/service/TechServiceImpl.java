package com.example.Foundation.service;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.repositories.TechnologiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TechServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service
    // private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private TechnologiesRepository technologiesRepository;

    public Technologies saveTechnology(Technologies technologies, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            technologies.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(fileName, file);
        }
        return technologiesRepository.save(technologies);
    }

    public List<Technologies> getAllTechnologies() {
        List<Technologies> all = technologiesRepository.findAll();
        return all;
    }

    public Technologies updateTechnology(int techId, Technologies technologies) throws IOException {
        Optional<Technologies> optionalArticle = technologiesRepository.findById(techId);
        if (optionalArticle.isPresent()) {
            Technologies existingTech = optionalArticle.get();
            existingTech.setCertification(technologies.getCertification());
            existingTech.setTechTitle(technologies.getTechTitle());
            if (technologies.getImage() != null) {
                existingTech.setImage(technologies.getImage());
            }
            return technologiesRepository.save(existingTech);
        } else {
            throw new IllegalArgumentException("Article not found with ID: " + techId);
        }
    }

    public Technologies getTechnologyById(int techId) {
        Technologies tech = technologiesRepository.findById(techId).orElseThrow(() -> new RuntimeException("technology not found"));
        return tech;
    }
}
