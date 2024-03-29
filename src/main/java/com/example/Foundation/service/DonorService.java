package com.example.Foundation.service;

import com.example.Foundation.exception.DonorNotFoundException;
import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DonorService {



    Donor createDonor(Donor donor, MultipartFile file) throws IOException;

    Donor updateDonor(int donorId, Donor donor,MultipartFile file) throws InvalidDonorIdException, IOException;

    List<Donor> getAllDonors();

    Donor getDonorById(int donorId) throws InvalidDonorIdException;

    String deleteDonorById(int donorId) throws DonorNotFoundException;

    Donor login(String emailAddress, String password);
    Donor getDonorByEmail(String emailAddress);
}
