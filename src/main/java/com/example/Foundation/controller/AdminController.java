package com.example.Foundation.controller;


import com.example.Foundation.exception.AdminNotFoundException;
import com.example.Foundation.exception.InvalidAdminIdException;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.service.AdminServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AdminController {

    final static Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/register/admin")
    public ResponseEntity<?> createAdmin(@Valid @ModelAttribute Admin admin, @RequestParam(required = false) MultipartFile file) throws IOException {
        admin.setPassword(this.bCryptPasswordEncoder.encode(admin.getPassword()));
        Admin createdAdmin = adminService.createAdmin(admin, file);
        log.info("admin created" + ":" + createdAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(createdAdmin);
    }

    @PatchMapping("/admin/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable int adminId, @ModelAttribute Admin adminX, @RequestParam(required = false) MultipartFile file) throws AdminNotFoundException, IOException {
        Admin admin = adminService.updateAdmin(adminId, adminX, file);
        return ResponseEntity.status(HttpStatus.OK).body(admin);

    }

    @GetMapping("/admins")
    public ResponseEntity<?> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.status(HttpStatus.OK).body(admins);
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<?> getAdminById(@PathVariable int adminId) throws InvalidAdminIdException {
        Admin admin = adminService.getAdminById(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(admin);
    }

    @DeleteMapping("/admin/{adminId}")
    public ResponseEntity<?> deleteAdminById(@PathVariable int adminId) throws AdminNotFoundException {
        adminService.deleteAdminById(adminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
