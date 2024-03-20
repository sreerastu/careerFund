package com.example.Foundation.repositories;

import com.example.Foundation.modal.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlackList, Long> {

    TokenBlackList findByToken(String token);
}
