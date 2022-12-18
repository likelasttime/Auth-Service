package winterdevcamp.Auth.Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.UserRole;
import winterdevcamp.Auth.Service.repository.MemberRepository;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    MemberRepository memberRepository;

    @Override
    public Page<Member> getUsersInfo(Pageable pageable){
        return memberRepository.findAllByRoleNotLike(UserRole.ROLE_NOT_PERMITTED, pageable);
    }
}
