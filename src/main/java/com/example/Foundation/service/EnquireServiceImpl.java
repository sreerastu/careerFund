package com.example.Foundation.service;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.modal.Enquire;
import com.example.Foundation.repositories.EnquireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EnquireServiceImpl {

    @Autowired
    private EnquireRepository enquireRepository;

    public Enquire saveEnquire(Enquire enquire, MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            enquire.setImage(imageFile.getBytes());
        }
        return enquireRepository.save(enquire);
    }

    public List<Enquire> getAllEnquires() {
        return enquireRepository.findAll();
    }

    public Enquire getEnquireById(int id) {
        return enquireRepository.findById(id).orElse(null);
    }

    public Enquire updateEnquire(int id, Enquire enquireDetails, MultipartFile imageFile) throws IOException {
        Optional<Enquire> optionalEnquire = enquireRepository.findById(id);
        if (optionalEnquire.isPresent()) {
            Enquire enquire = optionalEnquire.get();
            enquire.setName(enquireDetails.getName());
            enquire.setEmailAddress(enquireDetails.getEmailAddress());
            enquire.setContactNumber(enquireDetails.getContactNumber());
            enquire.setDescription(enquireDetails.getDescription());
            if (imageFile != null) {
                enquire.setImage(imageFile.getBytes());
            }
            return enquireRepository.save(enquire);
        }
        return null;
    }

    public void deleteEnquire(int id) {
        enquireRepository.deleteById(id);
    }
}
