package com.example.userserver.controller;

import com.example.userserver.controller.validation.SignUpFormValidator;
import com.example.userserver.model.Member;
import com.example.userserver.model.Response;
import com.example.userserver.model.request.RequestVerifyEmail;
import com.example.userserver.model.request.SignUpForm;
import com.example.userserver.service.SignUpService;
import com.example.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-server/user")
@RestController
public class SignUpController {
    private final SignUpService signUpService;
    private final SignUpFormValidator signUpFormValidator;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            String errMsg = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
        }
        signUpFormValidator.validate(signUpForm, errors);
        if(errors.hasErrors()){
            String errMsg = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
        }
        try{
            signUpService.signUpMember(signUpForm);
            log.info("회원가입 성공");
        }catch(Exception e){
            log.info("회원가입을 하는 도중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입을 하는 도중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public Response verify(@RequestBody RequestVerifyEmail requestVerifyEmail){
        Response response;
        String username = requestVerifyEmail.getUsername();
        try{
            Member member = userService.findByUsername(username);
            signUpService.sendVerificationMail(member);
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
            signUpService.verifyEmail(key);
            log.info("인증메일을 성공적으로 확인했습니다.");
            response = new Response("success", "인증메일을 성공적으로 확인했습니다.", null);
        }catch(Exception e){
            log.info("인증메일을 확인하는데 실패했습니다.");
            response = new Response("error", "인증메일을 확인하는데 실패했습니다.", null);
        }
        return response;
    }

}
