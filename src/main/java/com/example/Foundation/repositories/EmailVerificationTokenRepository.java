package com.example.Foundation.repositories;

import com.example.Foundation.modal.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Integer> {

    @Query("SELECT ev " +
            "FROM EmailVerificationToken ev " +
            "LEFT JOIN ev.donor d " +
            "LEFT JOIN ev.trainer t " +
            "LEFT JOIN ev.student s " +
            "LEFT JOIN ev.admin a " +
            "WHERE d.emailAddress = :emailAddress OR " +
            "      t.emailAddress = :emailAddress OR " +
            "      s.emailAddress = :emailAddress OR " +
            "      a.emailAddress = :emailAddress")
    List<EmailVerificationToken> findByAssociatedEmailAddress(@Param("emailAddress") String emailAddress);
}
