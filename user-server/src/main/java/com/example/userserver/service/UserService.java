package com.example.userserver.service;

import com.example.userserver.model.Member;
import com.example.userserver.model.request.UpdateMemberRequest;
import javassist.NotFoundException;

public interface UserService {
    void updateMemberInfo(UpdateMemberRequest request);

    boolean isPasswordEqual(String username, String requestPwd);

    void removeMember(String Username);

    Member findByUsername(String username) throws NotFoundException;

    void changePassword(Member member, String password) throws NotFoundException;

    public void requestChangePassword(Member member) throws NotFoundException;

    public boolean isPasswordUuidValidate(String key);
}
