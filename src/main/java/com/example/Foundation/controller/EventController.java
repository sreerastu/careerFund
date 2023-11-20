package com.example.Foundation.controller;

import com.example.Foundation.modal.Events;
import com.example.Foundation.service.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class EventController {

    @Autowired
    private EventServiceImpl imageService;

    @PostMapping("event/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            Events image = new Events();
            image.setImage(file.getBytes());
            imageService.saveImage(image);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error uploading image");
        }
    }

    @GetMapping("event/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        Optional<Events> image = imageService.getImageById(id);
        if (image.isPresent()) {
            byte[] imageData = image.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your image type

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
