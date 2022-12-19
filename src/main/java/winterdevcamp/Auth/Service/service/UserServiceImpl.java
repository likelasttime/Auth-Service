package winterdevcamp.Auth.Service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.request.UpdateMemberRequest;
import winterdevcamp.Auth.Service.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final MemberRepository memberRepository;

    private final SaltUtil saltUtil;

    @Override
    public void updateMemberInfo(UpdateMemberRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername());
        String encodePwd = saltUtil.encodePassword(member.getSalt().getSalt(), request.getPassword());
        member.updateInfo(request.getName(), encodePwd);
    }
}
