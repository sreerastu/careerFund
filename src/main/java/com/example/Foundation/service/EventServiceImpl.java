package com.example.Foundation.service;

import com.example.Foundation.modal.Events;
import com.example.Foundation.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl {

    @Autowired
    private EventRepository eventRepository;

    public Events saveImage(Events events) {
        return eventRepository.save(events);
    }

    public Optional<Events> getImageById(int eventId) {
        return eventRepository.findById(eventId);
    }
}
