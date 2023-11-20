package com.example.Foundation.service;

import com.example.Foundation.exception.AdminNotFoundException;
import com.example.Foundation.exception.InvalidAdminIdException;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService, UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(int adminId, Admin admin) throws AdminNotFoundException {

        Admin existingAdmin = adminRepository.findById(adminId).orElseThrow(() -> new AdminNotFoundException("Invalid adminId"));
        existingAdmin.setFirstName(admin.getFirstName());
        existingAdmin.setLastName(admin.getLastName());
        existingAdmin.setContactNumber(admin.getContactNumber());
        existingAdmin.setGender(admin.getGender());
        existingAdmin.setEmailAddress(admin.getEmailAddress());
        existingAdmin.setPassword(this.bCryptPasswordEncoder.encode(admin.getPassword()));
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
        return adminRepository.findByEmailAddressAndPassword(emailAddress,password);
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