package com.example.Foundation.controller;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.service.TechServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/technologies")
public class TechController {

    @Autowired
    private TechServiceImpl techService;

    @PostMapping("/techUpload")
    public ResponseEntity<Technologies> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("description") String description) {
        try {
            byte[] compressedImageBytes = techService.compressImage(file.getBytes());

            Technologies tech = new Technologies();
            tech.setImage(compressedImageBytes);
            tech.setDescription(description);
            Technologies technologies = techService.saveImage(tech);

            return ResponseEntity.status(HttpStatus.OK).body(technologies);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{techId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int techId) {
        Optional<Technologies> tech = techService.getImageById(techId);
        if (tech.isPresent()) {
            byte[] imageBytes = techService.getImageById(techId).get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your image type

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Technologies>> getAllTechnologies() {
        List<Technologies> allTechnologies = techService.getAllTechnologies();
        return ResponseEntity.ok(allTechnologies);
    }
}
