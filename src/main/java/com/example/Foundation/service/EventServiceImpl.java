package com.example.Foundation.service;

import com.example.Foundation.modal.Events;
import com.example.Foundation.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Autowired
    private EventRepository eventRepository;

    @Value("${aws.s3.EventsFolder}")
    private String folderName;

    public Events saveEvent(Events events, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            events.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return eventRepository.save(events);
    }

    public Events updateEvents(int eventId, Events events, MultipartFile file) throws IOException {
        Events eventsX = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Events not found"));

        if (events.getTitle() != null) {
            eventsX.setTitle(events.getTitle());
        }
        if (events.getDescription() != null) {
            eventsX.setDescription(events.getDescription());
        }
        if (events.getDate() != null) {
            eventsX.setDate(events.getDate());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = events.getImage();
            String newImageName = file.getOriginalFilename();
            eventsX.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return eventRepository.save(eventsX);
    }

    public Optional<Events> getEventById(int eventId) {
        return eventRepository.findById(eventId);
    }


    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }
}
