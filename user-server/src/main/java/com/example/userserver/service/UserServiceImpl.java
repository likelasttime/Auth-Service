package com.example.userserver.service;

import com.example.userserver.model.Member;
import com.example.userserver.model.Salt;
import com.example.userserver.model.request.UpdateMemberRequest;
import com.example.userserver.repository.MemberRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    private final SaltUtil saltUtil;

    private final RedisUtil redisUtil;

    private final EmailService emailService;

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

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = memberRepository.findByUsername(username);
        if(member == null) throw new NotFoundException("멤버가 조회되지 앟음");
        return member;
    }

    @Override
    public void changePassword(Member member, String password) throws NotFoundException {
        if(member == null) throw new NotFoundException("changePassword(), 멤버가 조회되지 않음");
        String salt = saltUtil.genSalt();
        member.updateSaltAndPassword(new Salt(salt), saltUtil.encodePassword(salt, password));
    }

    @Override
    public void requestChangePassword(Member member) throws NotFoundException{
        String CHANGE_PASSWORD_LINK = "http://localhost:5555/user-server/user/password/";
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        String key = UUID.randomUUID().toString();
        redisUtil.setDataExpire(key, member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(), "사용자 비밀번호 안내 메일", CHANGE_PASSWORD_LINK + key);
    }

    @Override
    public boolean isPasswordUuidValidate(String key){
        String memberId = redisUtil.getData(key);
        return !memberId.equals("");
    }
}
