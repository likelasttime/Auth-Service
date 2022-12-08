package winterdevcamp.Auth.Service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Response;
import winterdevcamp.Auth.Service.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/user")
public class MemberController {
    @Autowired
    private AuthService authService;

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
}
