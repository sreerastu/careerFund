package com.example.Foundation.controller;

import com.example.Foundation.modal.Technologies;
import com.example.Foundation.service.TechServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechController {

    @Autowired
    private TechServiceImpl techService;

    @PostMapping("/techUpload")
    public ResponseEntity<Technologies> handleFileUpload(@ModelAttribute Technologies technologies, @RequestParam(required = false) MultipartFile file) throws IOException {
        Technologies technologiesX = techService.saveTechnology(technologies, file);

        return ResponseEntity.status(HttpStatus.OK).body(technologiesX);
    }

    @GetMapping("/{techId}")
    public ResponseEntity<?> getTechnology(@PathVariable int techId) {
        Technologies tech = techService.getTechnologyById(techId);

        return ResponseEntity.status(HttpStatus.OK).body(tech);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Technologies>> getAllTechnologies() {
        List<Technologies> allTechnologies = techService.getAllTechnologies();
        return ResponseEntity.ok(allTechnologies);
    }
}
