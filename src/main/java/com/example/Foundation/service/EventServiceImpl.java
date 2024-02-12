package com.example.Foundation.service;

import com.example.Foundation.modal.Donor;
import com.example.Foundation.modal.Events;
import com.example.Foundation.repositories.EventRepository;
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
public class EventServiceImpl {

    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private EventRepository eventRepository;

    public Events saveEvent(Events events, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            events.setImage(fileName);
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
