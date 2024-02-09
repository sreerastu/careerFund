package com.example.Foundation.controller;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @PostMapping("/add")
    public Clients addClients(@ModelAttribute Clients clients,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return clientService.saveClient(clients, imageFile);
    }

    @GetMapping("/all")
    public List<Clients> getAllEnquires() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Clients getClientsById(@PathVariable int id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/update/{id}")
    public Clients updateEnquire(@PathVariable int id, @ModelAttribute Clients clients,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return clientService.updateClient(id, clients, imageFile);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClients(@PathVariable int id) {
        clientService.deleteClient(id);
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<ByteArrayResource> getClientImageById(@PathVariable int id) {
        byte[] imageBytes = clientService.getClientImageById(id);
        if (imageBytes != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust MediaType based on your image type
                    .body(new ByteArrayResource(imageBytes));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
