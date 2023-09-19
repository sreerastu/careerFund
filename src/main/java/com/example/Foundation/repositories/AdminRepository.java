package com.example.Foundation.repositories;

import com.example.Foundation.modal.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmailAddressAndPassword(String emailAddress, String password);

    Admin findByEmailAddress(String emailAddress);
}

