package com.example.Foundation.repositories;

import com.example.Foundation.modal.Admin;
import com.example.Foundation.modal.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Integer> {

    Donor findByEmailAddressAndPassword(String emailAddress, String password);

    Donor findByEmailAddress(String emailAddress);

}
