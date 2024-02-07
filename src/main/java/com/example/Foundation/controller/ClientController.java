package com.example.Foundation.controller;

import com.example.Foundation.modal.Clients;

import com.example.Foundation.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientServiceImpl enquireService;

    @PostMapping("/add")
    public Clients addEnquire(@ModelAttribute Clients enquire,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return enquireService.saveEnquire(enquire, imageFile);
    }

    @GetMapping("/all")
    public List<Clients> getAllEnquires() {
        return enquireService.getAllEnquires();
    }

    @GetMapping("/{id}")
    public Clients getEnquireById(@PathVariable int id) {
        return enquireService.getEnquireById(id);
    }

    @PutMapping("/update/{id}")
    public Clients updateEnquire(@PathVariable int id, @ModelAttribute Clients enquireDetails,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return enquireService.updateEnquire(id, enquireDetails, imageFile);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEnquire(@PathVariable int id) {
        enquireService.deleteEnquire(id);
    }
}
