package com.example.auth.service;

import com.example.auth.model.Member;
import com.example.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;

    private final SaltUtil saltUtil;

    @Override
    public Member loginMember(String id, String password) throws Exception{
        Member member = memberRepository.findByUsername(id);
        if(member == null) throw new Exception ("멤버가 조회되지 않음");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if(!member.getPassword().equals(password)) throw new Exception ("비밀번호가 다릅니다.");

        return member;
    }
}
