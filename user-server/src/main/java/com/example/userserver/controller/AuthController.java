package com.example.userserver.controller;

import com.example.userserver.controller.validation.SignUpFormValidator;
import com.example.userserver.model.Member;
import com.example.userserver.model.Response;
import com.example.userserver.model.request.RequestChangePassword1;
import com.example.userserver.model.request.RequestChangePassword2;
import com.example.userserver.model.request.SignUpForm;
import com.example.userserver.service.SignUpService;
import com.example.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user-server/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignUpService signUpService;
    private final SignUpFormValidator signUpFormValidator;
    private final UserService userService;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody @Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            String errMsg = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return new Response("error", errMsg, null);
        }
        signUpFormValidator.validate(signUpForm, errors);
        if(errors.hasErrors()){
            String errMsg = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return new Response("error", errMsg, null);
        }
        try{
            signUpService.signUpMember(signUpForm);
            log.info("회원가입 성공");
        }catch(Exception e){
            log.info("회원가입을 하는 도중 오류가 발생했습니다.");
            return new Response("error", e.getMessage(), null);
        }
        return new Response("success", "회원 가입 성공", null);
    }

    @PostMapping("/verify/{key}")
    public Response getVerify(@PathVariable String key){
        Response response;
        try{
            signUpService.verifyEmail(key);
            log.info("인증메일을 성공적으로 확인했습니다.");
            response = new Response("success", "인증메일을 성공적으로 확인했습니다.", null);
        }catch(Exception e){
            log.info("인증메일을 확인하는데 실패했습니다.");
            response = new Response("error", "인증메일을 확인하는데 실패했습니다.", null);
        }
        return response;
    }

    @PostMapping("/password")
    public Response requestChangePassword(@RequestBody RequestChangePassword1 requestChangePassword1){
        Response response;
        String username = "";
        try{
            username = requestChangePassword1.getUsername();
            Member member = userService.findByUsername(username);
            if(!member.getEmail().equals(requestChangePassword1.getEmail())) throw new NoSuchFieldException("");
            userService.requestChangePassword(member);
            log.info("성공적으로 " + username + "의 비밀번호 변경요청을 수행");
            response = new Response("success", "성공적으로 사용자의 비밀번호 변경요청을 수행했습니다.", null);
        }catch(NoSuchFieldException e){
            log.info("사용자 정보를 조회할 수 없습니다");
            response = new Response("error", "사용자 정보를 조회할 수 없습니다.", null);
        }catch(Exception e){
            log.info(username + " 비밀번호 변경요청을 수행할 수 없습니다");
            response = new Response("error", "비밀번호 변경 요청을 할 수 없습니다.", null);
        }
        return response;
    }

    @PostMapping("/password/{key}")
    public Response isPasswordUUIdValidate(@PathVariable String key){
        Response response;
        try{
            if(userService.isPasswordUuidValidate(key))
                response = new Response("success", "정상적인 접근입니다.", null);
            else
                response = new Response("error", "유효하지 않은 Key 값입니다.", null);
        }catch (Exception e){
            response = new Response("error", "유효하지 않은 key 값입니다.", null);
        }
        return response;
    }

    @PutMapping("/password")
    public Response changePassword(@RequestBody RequestChangePassword2 requestChangePassword2){
        Response response;
        String username = "";
        try{
            username = requestChangePassword2.getUsername();
            Member member = userService.findByUsername(username);
            userService.changePassword(member, requestChangePassword2.getPassword());
            log.info("성공적으로" + username + "의 비밀번호를 변경했습니다.");
            response = new Response("success", "성공적으로 사용자의 비밀번호를 변경했습니다.", null);
        }catch(Exception e){
            log.info(username + "의 비밀번호를 변경하지 못했습니다.");
            response = new Response("error", "사용자의 비밀번호를 변경할 수 없습니다.", null);
        }
        return response;
    }
}
