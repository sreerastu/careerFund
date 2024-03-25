package com.example.Foundation.service;

import com.example.Foundation.exception.AdminNotFoundException;
import com.example.Foundation.exception.InvalidAdminIdException;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService, UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Value("${aws.s3.AdminFolder}")
    private String folderName;


    @Override
    public Admin createAdmin(Admin admin, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            admin.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(int adminId, Admin admin, MultipartFile file) throws AdminNotFoundException, IOException {

        Admin existingAdmin = adminRepository.findById(adminId).orElseThrow(() -> new AdminNotFoundException("Invalid adminId"));
        if (admin.getFirstName() != null) {
            existingAdmin.setFirstName(admin.getFirstName());
        }
        if (admin.getLastName() != null) {
            existingAdmin.setLastName(admin.getLastName());
        }
        if (admin.getGender() != null) {
            existingAdmin.setGender(admin.getGender());
        }
        if (admin.getEmailAddress() != null) {
            existingAdmin.setEmailAddress(admin.getEmailAddress());
        }
        if (admin.getPassword() != null) {
            existingAdmin.setPassword(this.bCryptPasswordEncoder.encode(admin.getPassword()));
        }
        if (admin.getContactNumber() != null) {
            existingAdmin.setContactNumber(admin.getContactNumber());
        }
        if(admin.getUserType() !=null){
            existingAdmin.setUserType(admin.getUserType());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = admin.getImage();
            String newImageName = file.getOriginalFilename();
            existingAdmin.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return adminRepository.save(existingAdmin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(int adminId) throws InvalidAdminIdException {
        return adminRepository.findById(adminId).orElseThrow(() -> new InvalidAdminIdException("Invalid adminId"));
    }

    @Override
    public String deleteAdminById(int adminId) throws AdminNotFoundException {
        try {
            adminRepository.deleteById(adminId);
            return "Admin Successfully deleted";
        } catch (Exception ex) {
            throw new AdminNotFoundException("Invalid adminId");
        }
    }

    @Override
    public Admin login(String emailAddress, String password) {
        return adminRepository.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public Admin getAminByMail(String emailAddress) {
        return adminRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmailAddress(emailAddress);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found with email address: " + emailAddress);
        }
        return new org.springframework.security.core.userdetails.User(admin.getEmailAddress(), admin.getPassword(), admin.getAuthorities());
    }

}