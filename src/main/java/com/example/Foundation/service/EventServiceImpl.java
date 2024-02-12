package com.example.Foundation.service;

import com.example.Foundation.modal.Events;
import com.example.Foundation.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    //   private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private EventRepository eventRepository;

    public Events saveEvent(Events events, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            events.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(fileName, file);
        }
        return eventRepository.save(events);
    }

    public Optional<Events> getEventById(int eventId) {
        return eventRepository.findById(eventId);
    }


    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }
}
