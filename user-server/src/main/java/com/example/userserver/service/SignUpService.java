package com.example.userserver.service;

import com.example.userserver.model.Member;
import com.example.userserver.model.UserRole;
import com.example.userserver.model.request.SignUpForm;
import javassist.NotFoundException;

public interface SignUpService {
    void signUpMember(SignUpForm signUpForm);

    void verifyEmail(String key) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    void sendVerificationMail(Member member) throws NotFoundException;

}
