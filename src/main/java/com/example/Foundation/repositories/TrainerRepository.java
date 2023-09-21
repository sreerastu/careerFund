package com.example.Foundation.repositories;

import com.example.Foundation.modal.Admin;
import com.example.Foundation.modal.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    Trainer findByEmailAddressAndPassword(String emailAddress, String password);

    Trainer findByEmailAddress(String emailAddress);

}