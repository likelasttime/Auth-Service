package winterdevcamp.Auth.Service.service;

import winterdevcamp.Auth.Service.model.request.UpdateMemberRequest;

public interface UserService {
    void updateMemberInfo(UpdateMemberRequest request);

    boolean isPasswordEqual(String username, String requestPwd);

    void removeMember(String Username);
}
