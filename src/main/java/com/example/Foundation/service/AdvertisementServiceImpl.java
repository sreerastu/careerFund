package com.example.Foundation.service;

import com.example.Foundation.modal.Advertisement;
import com.example.Foundation.repositories.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementServiceImpl {
    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    //   private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public Advertisement saveAdvertisement(Advertisement advertisement, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            advertisement.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(fileName, file);
        }
        return advertisementRepository.save(advertisement);
    }

    public Optional<Advertisement> getAdvertisementById(int advertisementId) {
        return advertisementRepository.findById(advertisementId);
    }

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

}
