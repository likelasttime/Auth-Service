package winterdevcamp.Auth.Service.service;

import javassist.NotFoundException;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.UserRole;

public interface AuthService {
    void signUpMember(Member member);

    Member loginMember(String id, String password) throws Exception;

    void sendVerificationMail(Member member) throws NotFoundException;

    void verifyEmail(String key) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    Member findByUsername(String username) throws NotFoundException;

    boolean isPasswordUuidValidate(String key);

    void changePassword(Member member, String password) throws NotFoundException;

    void requestChangePassword(Member member) throws NotFoundException;
}
