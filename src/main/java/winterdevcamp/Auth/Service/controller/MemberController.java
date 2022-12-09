package winterdevcamp.Auth.Service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Response;
import winterdevcamp.Auth.Service.model.request.RequestLoginMember;
import winterdevcamp.Auth.Service.service.AuthService;
import winterdevcamp.Auth.Service.service.CookieUtil;
import winterdevcamp.Auth.Service.service.JwtUtil;
import winterdevcamp.Auth.Service.service.RedisUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/user")
public class MemberController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody Member member){
        try{
            authService.signUpMember(member);
            log.info("회원가입 성공");
            return new Response("success", "회원가입을 성공적으로 완료했습니다.", null);
        }catch(Exception e){
            log.info("회원가입 실패");
            return new Response("error", "회원가입을 하는 도중 오류가 발생했습니다.", null);
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody RequestLoginMember user,
                          HttpServletRequest req,
                          HttpServletResponse res){
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
            return new Response("success", "로그인에 성공했습니다.", token);
        }catch(Exception e){
            return new Response("error", "로그인에 실패했습니다.", e.getMessage());
        }
    }
}
