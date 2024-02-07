package com.example.Foundation.controller;

import com.example.Foundation.modal.Enquire;

import com.example.Foundation.service.EnquireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/enquire")
public class EnquireController {

    @Autowired
    private EnquireServiceImpl enquireService;

    @PostMapping("/add")
    public Enquire addEnquire(@ModelAttribute Enquire enquire,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return enquireService.saveEnquire(enquire, imageFile);
    }

    @GetMapping("/all")
    public List<Enquire> getAllEnquires() {
        return enquireService.getAllEnquires();
    }

    @GetMapping("/{id}")
    public Enquire getEnquireById(@PathVariable int id) {
        return enquireService.getEnquireById(id);
    }

    @PutMapping("/update/{id}")
    public Enquire updateEnquire(@PathVariable int id, @ModelAttribute Enquire enquireDetails,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return enquireService.updateEnquire(id, enquireDetails, imageFile);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEnquire(@PathVariable int id) {
        enquireService.deleteEnquire(id);
    }
}
