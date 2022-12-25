package com.example.userserver.service;

import com.example.userserver.model.Member;
import com.example.userserver.model.UserRole;
import com.example.userserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final MemberRepository memberRepository;

    @Override
    public Page<Member> getUsersInfo(Pageable pageable){
        return memberRepository.findAllByRoleNotLike(UserRole.ROLE_NOT_PERMITTED, pageable);
    }
}
