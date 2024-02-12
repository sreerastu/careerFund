package com.example.Foundation.service;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.repositories.TechnologiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class TechServiceImpl {
    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private TechnologiesRepository technologiesRepository;

    public Technologies saveTechnology(Technologies technologies, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            technologies.setImage(fileName);
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

    public Technologies getTechnologyById(int techId){
        Technologies tech =technologiesRepository.findById(techId).orElseThrow(()->new RuntimeException("technology not found"));
        return tech;
    }
}
