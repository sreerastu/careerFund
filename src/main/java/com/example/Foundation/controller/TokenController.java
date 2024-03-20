package com.example.Foundation.controller;

import com.example.Foundation.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/blacklist/add")
    public void addToBlacklist(@RequestBody String token) {
        tokenBlacklistService.addToBlacklist(token);
    }

    @PostMapping("/blacklist/check")
    public boolean isBlacklisted(@RequestBody String token) {
        return tokenBlacklistService.isBlacklisted(token);
    }
}
