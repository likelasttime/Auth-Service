package winterdevcamp.Auth.Service.service;

import winterdevcamp.Auth.Service.model.request.UpdateMemberRequest;

public interface UserService {
    void updateMemberInfo(UpdateMemberRequest request);
}
