package com.example.Foundation.repositories;


import com.example.Foundation.modal.Clients;
import com.example.Foundation.modal.Enquire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Clients,Integer> {
}
