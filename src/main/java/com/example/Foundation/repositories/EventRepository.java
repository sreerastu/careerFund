package com.example.Foundation.repositories;

import com.example.Foundation.modal.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Events, Integer> {

}
