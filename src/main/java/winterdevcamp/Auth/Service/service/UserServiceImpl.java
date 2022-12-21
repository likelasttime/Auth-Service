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

    @Override
    public boolean isPasswordEqual(String username, String requestPwd){
        Member member = memberRepository.findByUsername(username);
        String salt = member.getSalt().getSalt();
        String encodeRequest = saltUtil.encodePassword(salt, requestPwd);
        if(!member.getPassword().equals(encodeRequest)){
            return false;
        }
        return true;
    }

    @Override
    public void removeMember(String username){
        memberRepository.deleteByUsername(username);
    }
}
