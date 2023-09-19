package com.example.Foundation.service;

import com.example.Foundation.exception.AdminNotFoundException;
import com.example.Foundation.exception.InvalidAdminIdException;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;


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
        //existingAdmin.setDob(admin.getDob());
        existingAdmin.setEmailAddress(admin.getEmailAddress());
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

}