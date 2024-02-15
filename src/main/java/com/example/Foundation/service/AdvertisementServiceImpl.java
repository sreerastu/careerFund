package com.example.Foundation.service;

import com.example.Foundation.modal.Advertisement;
import com.example.Foundation.repositories.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementServiceImpl {
    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Value("${aws.s3.AdvertisementFolder}")
    private String folderName;

    public Advertisement saveAdvertisement(Advertisement advertisement, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            advertisement.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return advertisementRepository.save(advertisement);
    }

    public Advertisement updateAdvertisement(int adId, Advertisement advertisement, MultipartFile file) throws IOException {
        Advertisement advertisementX = advertisementRepository.findById(adId).orElseThrow(() -> new RuntimeException("Advertisement not found"));

        if (advertisement.getName() != null) {
            advertisementX.setName(advertisement.getName());
        }
        if (advertisement.getContactNumber() != null) {
            advertisementX.setContactNumber(advertisement.getContactNumber());
        }
        if (advertisement.getEmailAddress() != null) {
            advertisementX.setEmailAddress(advertisement.getEmailAddress());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = advertisement.getImage();
            String newImageName = file.getOriginalFilename();
            advertisementX.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return advertisementRepository.save(advertisementX);
    }

    public Optional<Advertisement> getAdvertisementById(int advertisementId) {
        return advertisementRepository.findById(advertisementId);
    }

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

}
