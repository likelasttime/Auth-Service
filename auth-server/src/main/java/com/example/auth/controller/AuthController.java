package com.example.auth.controller;

import com.example.auth.model.Member;
import com.example.auth.model.Response;
import com.example.auth.model.request.*;
import com.example.auth.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth-server/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public Response login(@RequestBody RequestLoginMember user, HttpServletResponse res, HttpServletRequest req){
        try{
            String username = user.getUsername();
            final Member member = authService.loginMember(username, user.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);
            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);
            log.info(username + " 로그인 성공");
            if(member.getRole().name().equals("ROLE_NOT_PERMITTED")) return new Response("success", "이메일 인증이 필요합니다.", token);
            return new Response("success", "로그인에 성공했습니다.", token);
        }catch(Exception e){
            return new Response("error", "로그인에 실패했습니다.", e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Response logout(HttpServletRequest request, @RequestBody Map<String, String> map) {
        String accessToken = cookieUtil.getCookie(request, "accessToken").getValue();
        String refreshToken = cookieUtil.getCookie(request, "refreshToken").getValue();
        return authService.logout(map.get("username"), accessToken, refreshToken);
    }
}
