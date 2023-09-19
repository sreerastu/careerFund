package com.example.Foundation.service;

import com.example.Foundation.exception.DonorNotFoundException;
import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;

import java.util.List;

public interface DonorService {



    Donor createDonor(Donor donor);

    Donor updateDonor(int donorId, Donor donor) throws InvalidDonorIdException;

    List<Donor> getAllDonors();

    Donor getDonorById(int donorId) throws InvalidDonorIdException;

    String deleteDonorById(int donorId) throws DonorNotFoundException;

    Donor login(String emailAddress, String password);
    Donor getDonorByEmail(String emailAddress);
}
