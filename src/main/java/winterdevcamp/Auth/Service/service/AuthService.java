package winterdevcamp.Auth.Service.service;

import winterdevcamp.Auth.Service.model.Member;

public interface AuthService {
    void signUpMember(Member member);

    Member loginMember(String id, String password) throws Exception;

}
