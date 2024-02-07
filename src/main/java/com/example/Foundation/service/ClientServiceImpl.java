package com.example.Foundation.service;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl {

    @Autowired
    private ClientsRepository enquireRepository;

    public Clients saveEnquire(Clients enquire, MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            enquire.setImage(imageFile.getBytes());
        }
        return enquireRepository.save(enquire);
    }

    public List<Clients> getAllEnquires() {
        return enquireRepository.findAll();
    }

    public Clients getEnquireById(int id) {
        return enquireRepository.findById(id).orElse(null);
    }

    public Clients updateEnquire(int id, Clients enquireDetails, MultipartFile imageFile) throws IOException {
        Optional<Clients> optionalEnquire = enquireRepository.findById(id);
        if (optionalEnquire.isPresent()) {
            Clients enquire = optionalEnquire.get();
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
