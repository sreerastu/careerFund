package com.example.Foundation.service;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ClientServiceImpl {

    @Autowired
    private ClientsRepository clientsRepository;

    public Clients saveClient(Clients client, MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            client.setImage(imageFile.getBytes());
        }
        return clientsRepository.save(client);
    }

    public List<Clients> getAllClients() {
        return clientsRepository.findAll();
    }

    public Clients getClientById(int id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Clients updateClient(int id, Clients clientDetails, MultipartFile imageFile) throws IOException {
        Clients client = clientsRepository.findById(id).orElse(null);
        if (client != null) {
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setContactNumber(clientDetails.getContactNumber());
            client.setPassword(clientDetails.getPassword());
            client.setCompanyName(clientDetails.getCompanyName());
            client.setDescription(clientDetails.getDescription());
            if (imageFile != null && !imageFile.isEmpty()) {
                client.setImage(imageFile.getBytes());
            }
            return clientsRepository.save(client);
        }
        return null;
    }

    public void deleteClient(int id) {
        clientsRepository.deleteById(id);
    }
}
