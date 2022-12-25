package com.example.userserver.service;

import com.example.userserver.model.Member;
import com.example.userserver.model.Salt;
import com.example.userserver.model.UserRole;
import com.example.userserver.model.request.SignUpForm;
import com.example.userserver.repository.MemberRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService{
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final SaltUtil saltUtil;
    private final RedisUtil redisUtil;

    @Override
    public void signUpMember(SignUpForm signUpForm){
        String password = signUpForm.getPassword();
        String salt = saltUtil.genSalt();
        Member member1 = Member.builder()
                .username(signUpForm.getUsername())
                .name(signUpForm.getName())
                .email(signUpForm.getEmail())
                .role(UserRole.ROLE_NOT_PERMITTED)
                .salt(Salt.builder().salt(salt).build())
                .password(saltUtil.encodePassword(salt, password))
                .build();

        memberRepository.save(member1);
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        modifyUserRole(member, UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole){
        member.updateRole(userRole);
    }

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(), "Auth service 회원가입 인증 메일 입니다.", VERIFICATION_LINK + uuid.toString());
    }
}
