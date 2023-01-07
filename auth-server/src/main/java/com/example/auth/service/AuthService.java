package com.example.auth.service;

import com.example.auth.model.Member;
import com.example.auth.model.Response;

public interface AuthService {

    Member loginMember(String id, String password) throws Exception;

    Response logout(String id, String accessToken, String refreshToken);
}
