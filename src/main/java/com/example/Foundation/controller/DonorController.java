package com.example.Foundation.controller;


import com.example.Foundation.exception.DonorNotFoundException;
import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.service.DonorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api")
public class DonorController {

    @Autowired
    private DonorServiceImpl donorService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register/donor")
    public ResponseEntity<?> createDonor(@Valid @ModelAttribute Donor donor,@RequestParam(required = false) MultipartFile file) throws IOException {
        donor.setPassword(this.bCryptPasswordEncoder.encode(donor.getPassword()));

        Donor createdDonor = donorService.createDonor(donor,file);
        return ResponseEntity.status(HttpStatus.OK).body(createdDonor);
    }

    @PatchMapping("/donor/{donorId}")
    public ResponseEntity<?> updateDonor(@PathVariable int donorId, @RequestBody Donor donorX, @RequestParam(required = false) MultipartFile file) throws InvalidDonorIdException, IOException {
        Donor donor = donorService.updateDonor(donorId, donorX,file);
        return ResponseEntity.status(HttpStatus.OK).body(donor);
    }

    @GetMapping("/donors")
    public ResponseEntity<?> getAllDonors() {
        List<Donor> donors = donorService.getAllDonors();
        return ResponseEntity.status(HttpStatus.OK).body(donors);
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<?> getDonorById(@PathVariable int donorId) throws InvalidDonorIdException {
        Donor donor = donorService.getDonorById(donorId);
        return ResponseEntity.status(HttpStatus.OK).body(donor);
    }

    @DeleteMapping("/Donor/{DonorId}")
    public ResponseEntity<?> deleteDonorById(@PathVariable int DonorId) throws DonorNotFoundException {
        donorService.deleteDonorById(DonorId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
