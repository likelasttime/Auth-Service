package com.example.auth.service;

import com.example.auth.model.Member;
import com.example.auth.model.Response;
import com.example.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;

    private final SaltUtil saltUtil;

    private final JwtUtil jwtUtil;

    private final RedisTemplate redisTemplate;

    private final MyUserDetailsService myUserDetailsService;

    @Override
    public Member loginMember(String id, String password) throws Exception{
        Member member = memberRepository.findByUsername(id);
        if(member == null) throw new Exception ("멤버가 조회되지 않음");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if(!member.getPassword().equals(password)) throw new Exception ("비밀번호가 다릅니다.");

        return member;
    }

    @Override
    public Response logout(String id, String accessToken, String refreshToken) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(id);

        if (!jwtUtil.validateToken(accessToken, userDetails)) {
            return new Response("error","잘못된 요청입니다.", null);
        }

        if (redisTemplate.opsForValue().get(refreshToken) != null) {
            redisTemplate.delete(refreshToken);
        }

        Long expiration = jwtUtil.getExpiredTime(accessToken).getTime();
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new Response("success","로그아웃 되었습니다.", null);
    }
}
