package com.example.Foundation.controller;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.service.ClientsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    @Autowired
    private ClientsServiceImpl clientsService;

    // Get all clients
    @GetMapping("/all")
    public ResponseEntity<List<Clients>> getAllClients() {
        List<Clients> clients = clientsService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // Get client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Clients> getClientById(@PathVariable int id) {
        Clients client = clientsService.getClientById(id);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new client
    @PostMapping("/save")
    public ResponseEntity<Clients> createClient(@ModelAttribute Clients client,
                                                @RequestParam(required = false) MultipartFile file) {
        try {
            Clients savedClient = clientsService.saveClient(client, file);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing client
    @PatchMapping("/update/{id}")
    public ResponseEntity<Clients> partialUpdateClient(@PathVariable int id,
                                                       @ModelAttribute Clients client,
                                                       @RequestParam(required = false) MultipartFile file) {
        try {
            Clients updatedClient = clientsService.partialUpdateClient(id, client, file);
            if (updatedClient != null) {
                return new ResponseEntity<>(updatedClient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a client by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        clientsService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getClientImage(@PathVariable int id) throws IOException {
        Clients client = clientsService.getClientById(id);
        if (client != null && client.getImage() != null) {
            Resource imageResource = clientsService.loadImageAsResource(client.getImage());
            if (imageResource != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageResource.getFilename() + "\"")
                        .body(imageResource);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
