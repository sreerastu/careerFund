package com.example.Foundation.controller;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientsService;

    @PostMapping("/add")
    public Clients addClient(@ModelAttribute Clients client,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return clientsService.saveClient(client, imageFile);
    }

    @GetMapping("/all")
    public List<Clients> getAllClients() {
        return clientsService.getAllClients();
    }

    @GetMapping("/{id}")
    public Clients getClientById(@PathVariable int id) {
        return clientsService.getClientById(id);
    }

    @PutMapping("/update/{id}")
    public Clients updateClient(@PathVariable int id, @ModelAttribute Clients clientDetails,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return clientsService.updateClient(id, clientDetails, imageFile);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable int id) {
        clientsService.deleteClient(id);
    }
}
