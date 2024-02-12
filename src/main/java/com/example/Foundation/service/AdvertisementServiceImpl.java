package com.example.Foundation.service;

import com.example.Foundation.modal.Advertisement;
import com.example.Foundation.repositories.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementServiceImpl {
    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public Advertisement saveAdvertisement(Advertisement advertisement, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            advertisement.setImage(fileName);
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
