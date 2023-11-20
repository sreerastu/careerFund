package com.example.Foundation.service;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.repositories.TechnologiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TechServiceImpl {

    @Autowired
    private TechnologiesRepository technologiesRepository;

    public Technologies saveImage(Technologies technologies) {
        try {
            byte[] compressedImageBytes = compressImage(technologies.getImage());
            technologies.setImage(compressedImageBytes);
            return technologiesRepository.save(technologies);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
            return null;
        }
    }

    public Optional<Technologies> getImageById(int id) {
        Optional<Technologies> technologiesOptional = technologiesRepository.findById(id);
        technologiesOptional.ifPresent(tech -> {
            try {
                byte[] compressedImageBytes = compressImage(tech.getImage());
                tech.setImage(compressedImageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception
            }
        });
        return technologiesOptional;
    }

    public List<Technologies> getAllTechnologies() {
        List<Technologies> all = technologiesRepository.findAll();
        return all;
    }

    public byte[] compressImage(byte[] originalImageBytes) throws IOException {
        // Convert the byte array to a BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);

        // Create a ByteArrayOutputStream to write the compressed image
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Compress the image
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f); // Adjust the quality as needed

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
        }

        // Get the compressed image bytes
        byte[] compressedImageBytes = bos.toByteArray();

        // Close streams
        bis.close();
        bos.close();

        return compressedImageBytes;
    }
}
