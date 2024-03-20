package com.example.Foundation.service;

import com.example.Foundation.modal.TokenBlackList;
import com.example.Foundation.repositories.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void addToBlacklist(String token) {
        TokenBlackList blacklist = new TokenBlackList(token);
        tokenBlacklistRepository.save(blacklist);
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token) != null;
    }
}
