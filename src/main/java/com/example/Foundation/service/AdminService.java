package com.example.Foundation.service;

import com.example.Foundation.exception.AdminNotFoundException;
import com.example.Foundation.exception.InvalidAdminIdException;
import com.example.Foundation.modal.Admin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {


    Admin createAdmin(Admin admin, MultipartFile file) throws IOException;

    Admin updateAdmin(int adminId, Admin admin) throws AdminNotFoundException;

    List<Admin> getAllAdmins();

    Admin getAdminById(int adminId) throws InvalidAdminIdException;

    String deleteAdminById(int adminId) throws AdminNotFoundException;

    Admin login(String emailAddress, String password) ;

    Admin getAminByMail(String emailAddress);

}
