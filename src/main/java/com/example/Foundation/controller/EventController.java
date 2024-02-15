package com.example.Foundation.controller;

import com.example.Foundation.modal.Events;
import com.example.Foundation.service.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    @PostMapping("/event/upload")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute Events events, @RequestParam(required = false) MultipartFile file) throws IOException {

        Events events1 = eventService.saveEvent(events, file);
        return ResponseEntity.status(HttpStatus.OK).body(events1);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable int articleId) {
        Events events = eventService.getEventById(articleId).orElseThrow(() -> new RuntimeException("event not found with Id" + ":" + articleId));

        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PatchMapping("/update{eventId}")
    public ResponseEntity<String> updateArticle(@PathVariable int eventId, @ModelAttribute Events updatedEvent, @RequestParam(required = false) MultipartFile file) throws IOException {
        Events updated = eventService.updateEvents(eventId, updatedEvent, file);
        return ResponseEntity.ok("Event updated with ID: " + updated.getEventId());
    }
}

