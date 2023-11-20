package com.example.Foundation.controller;

import com.example.Foundation.modal.Enquire;
import com.example.Foundation.service.EnquireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class EnquireController {

    @Autowired
    private EnquireServiceImpl enquireService;

    @PostMapping("/register/enquire")
    public ResponseEntity<?> createEnquire(@RequestBody Enquire enquire) {
        Enquire enquire1 = enquireService.saveEnquire(enquire);
        return ResponseEntity.status(HttpStatus.OK).body(enquire1);
    }

    @GetMapping("/enquires")
    public ResponseEntity<?> getAllEnquire() {
        List<Enquire> stories = enquireService.getAllEnquires();
        return ResponseEntity.status(HttpStatus.OK).body(stories);
    }

    @GetMapping("/enquire/{enquireId}")
    public ResponseEntity<?> getEnquire(@PathVariable int enquireId) {
        Optional<Enquire> enquire = enquireService.getEnquireById(enquireId);
        return ResponseEntity.status(HttpStatus.OK).body(enquire);
    }
}
