package com.example.Foundation.service;

import com.example.Foundation.modal.Enquire;
import com.example.Foundation.modal.Events;
import com.example.Foundation.modal.SuccessStories;
import com.example.Foundation.repositories.EnquireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnquireServiceImpl {

    @Autowired
    private EnquireRepository enquireRepository;

    public Enquire saveEnquire(Enquire enquire) {
        return enquireRepository.save(enquire);
    }

    public Optional<Enquire> getEnquireById(int enquireId) {
        return enquireRepository.findById(enquireId);
    }

    public List<Enquire> getAllEnquires() {
        return enquireRepository.findAll();
    }

}
