package winterdevcamp.Auth.Service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import winterdevcamp.Auth.Service.model.Member;

public interface UserInfoService {
    Page<Member> getUsersInfo(Pageable pageable);
}
