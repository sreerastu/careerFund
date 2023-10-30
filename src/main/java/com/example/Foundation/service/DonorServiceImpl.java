package com.example.Foundation.service;

import com.example.Foundation.exception.DonorNotFoundException;
import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.repositories.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorServiceImpl implements DonorService, UserDetailsService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Donor createDonor(Donor donor) {

        return donorRepository.save(donor);
    }

    @Override
    public Donor updateDonor(int donorId, Donor donor) throws InvalidDonorIdException {
        Donor existingDonor = donorRepository.findById(donor.getDonorId()).orElseThrow(() -> new InvalidDonorIdException("please enter a valid donorId"));

        existingDonor.setFirstName(donor.getFirstName());
        existingDonor.setLastName(donor.getLastName());
        existingDonor.setGender(donor.getGender());
        existingDonor.setContactNumber(donor.getContactNumber());
        existingDonor.setEmailAddress(donor.getEmailAddress());
        existingDonor.setPassword(this.bCryptPasswordEncoder.encode(donor.getPassword()));

        return existingDonor;
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Override
    public Donor getDonorById(int donorId) throws InvalidDonorIdException {
        return donorRepository.findById(donorId).orElseThrow(() -> new InvalidDonorIdException("Invalid donorId" + donorId));
    }

    @Override
    public String deleteDonorById(int donorId) throws DonorNotFoundException {
        try {
            donorRepository.deleteById(donorId);

        } catch (Exception ex) {
            throw new DonorNotFoundException("invalid donorId passed");
        }
        return "Donor Successfully deleted" + donorId;

    }

    @Override
    public Donor login(String emailAddress, String password) {
        return donorRepository.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public Donor getDonorByEmail(String emailAddress) {
        return donorRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Donor donor = donorRepository.findByEmailAddress(emailAddress);
        if (donor == null) {
            throw new UsernameNotFoundException("Donor not found with email address: " + emailAddress);
        }
        return new org.springframework.security.core.userdetails.User(donor.getEmailAddress(), donor.getPassword(), donor.getAuthorities());
    }
}

