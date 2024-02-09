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
    private ClientsRepository clientsRepository;

    public Clients saveClient(Clients clients, MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            clients.setImage(imageFile.getBytes());
        }
        return clientsRepository.save(clients);
    }

    public List<Clients> getAllClients() {
        return clientsRepository.findAll();
    }

    public Clients getClientById(int id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Clients updateClient(int id, Clients clients, MultipartFile imageFile) throws IOException {
        Optional<Clients> optionalEnquire = clientsRepository.findById(id);
        if (optionalEnquire.isPresent()) {
            Clients enquire = optionalEnquire.get();
            enquire.setName(clients.getName());
            enquire.setEmailAddress(clients.getEmailAddress());
            enquire.setContactNumber(clients.getContactNumber());
            enquire.setDescription(clients.getDescription());
            if (imageFile != null) {
                enquire.setImage(imageFile.getBytes());
            }
            return clientsRepository.save(enquire);
        }
        return null;
    }
    public byte[] getClientImageById(int id) {
        Optional<Clients> optionalClient = clientsRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get().getImage();
        }
        return null;
    }

    public void deleteClient(int id) {
        clientsRepository.deleteById(id);
    }
}
