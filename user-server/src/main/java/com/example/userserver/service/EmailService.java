package com.example.userserver.service;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
