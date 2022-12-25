package com.example.auth.service;

import com.example.auth.model.Member;
import com.example.auth.model.UserRole;
import javassist.NotFoundException;

public interface AuthService {

    Member loginMember(String id, String password) throws Exception;

}
