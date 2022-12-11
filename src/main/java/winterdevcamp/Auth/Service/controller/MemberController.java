package winterdevcamp.Auth.Service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Response;
import winterdevcamp.Auth.Service.model.request.RequestLoginMember;
import winterdevcamp.Auth.Service.model.request.RequestVerifyEmail;
import winterdevcamp.Auth.Service.service.AuthService;
import winterdevcamp.Auth.Service.service.CookieUtil;
import winterdevcamp.Auth.Service.service.JwtUtil;
import winterdevcamp.Auth.Service.service.RedisUtil;

import javax.servlet.http.Cookie;
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
    public Response login(@RequestBody RequestLoginMember user, HttpServletResponse res){
        try{
            String username = user.getUsername();
            final Member member = authService.loginMember(username, user.getPassword());
            if(member.getRole().name().equals("ROLE_NOT_PERMITTED")) return new Response("error", "이메일 인증이 필요합니다.", null);
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

    @PostMapping("/verify")
    public Response verify(@RequestBody RequestVerifyEmail requestVerifyEmail){
        Response response;
        String username = requestVerifyEmail.getUsername();
        try{
            Member member = authService.findByUsername(username);
            authService.sendVerificationMail(member);
            log.info(username + "에 인증메일 발송");
            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
        }catch(Exception exception){
            log.info(username + "에 인증메일 발송 실패");
            response = new Response("error", "인증메일을 보내는데 문제가 발생했습니다.", exception);
        }
        return response;
    }

    @PostMapping("/verify/{key}")
    public Response getVerify(@PathVariable String key){
        Response response;
        try{
            authService.verifyEmail(key);
            log.info("인증메일을 성공적으로 확인했습니다.");
            response = new Response("success", "인증메일을 성공적으로 확인했습니다.", null);
        }catch(Exception e){
            log.info("인증메일을 확인하는데 실패했습니다.");
            response = new Response("error", "인증메일을 확인하는데 실패했습니다.", null);
        }
        return response;
    }
}
