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
@RequestMapping("/api/images")
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    @PostMapping("event/upload")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute Events events, @RequestParam(required = false) MultipartFile file) throws IOException {

        Events events1 = eventService.saveEvent(events, file);
        return ResponseEntity.status(HttpStatus.OK).body(events1);
    }

    @GetMapping("event/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        Events events = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("event not found with Id" + ":" + id));

        return ResponseEntity.status(HttpStatus.OK).body(events);
    }
}

