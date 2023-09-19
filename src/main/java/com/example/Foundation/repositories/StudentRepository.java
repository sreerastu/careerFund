package com.example.Foundation.repositories;

import com.example.Foundation.modal.Admin;
import com.example.Foundation.modal.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByEmailAddressAndPassword(String emailAddress, String password);

    Student findByEmailAddress(String emailAddress);

}
