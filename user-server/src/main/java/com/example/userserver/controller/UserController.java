package com.example.userserver.controller;

import com.example.userserver.model.Member;
import com.example.userserver.model.Response;
import com.example.userserver.model.SecurityMember;
import com.example.userserver.model.UpdateMemberResponse;
import com.example.userserver.model.request.RequestVerifyEmail;
import com.example.userserver.model.request.UpdateMemberRequest;
import com.example.userserver.service.SignUpService;
import com.example.userserver.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RequestMapping(value="/user-server/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final SignUpService signUpService;
    private final UserService userService;

    @GetMapping("/email/{username}")
    public Response getEmail(@PathVariable String username){
        Response response;
        try{
            Member member = userService.findByUsername(username);
            response = new Response("success", "사용자의 이메일을 가져왔습니다.", member.getEmail());
        }catch(NotFoundException e){
            response = new Response("error", e.getMessage(), null);
        }
        return response;
    }

    @PostMapping("/verify")
    public Response verify(@RequestBody RequestVerifyEmail requestVerifyEmail){
        Response response;
        String username = "";
        try{
            username = requestVerifyEmail.getUsername();
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

    @GetMapping("/info")
    public Response getInfo(){
        Response response;
        try{
            SecurityMember securityMember = (SecurityMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = securityMember.getUsername();
            Member member = userService.findByUsername(username);
            UpdateMemberResponse updateMemberResponse = UpdateMemberResponse.builder()
                    .name(member.getName())
                    .email(member.getEmail())
                    .username(username)
                    .build();
            response = new Response("success", username + "의 정보를 성공적으로 가져왔습니다.", updateMemberResponse);
        }catch(Exception e){
            log.info("사용자의 정보를 가져오지 못했습니다.");
            response = new Response("error", "사용자의 정보를 가져오지 못했습니다.", null);
        }
        return response;
    }

    @PutMapping("/info")
    public Response updateInfo(@RequestBody UpdateMemberRequest updateMemberRequest){
        Response response;
        try{
            userService.updateMemberInfo(updateMemberRequest);
            log.info(updateMemberRequest.getUsername() + "의 정보 수정에 성공했습니다.");
            response = new Response("success", "성공적으로 사용자의 정보를 변경했습니다.", null);
        }catch(Exception e){
            log.info(e.getMessage());
            response = new Response("error", "사용자 정보 수정에 실패했습니다.", null);
        }
        return response;
    }

    @PostMapping("/remove")
    public Response removeMember(Principal principal, @RequestBody Map<String, String> map){
        Response response;
        try {
            String username = principal.getName();
            if(userService.isPasswordEqual(username, map.get("password"))) {
                userService.removeMember(username);
                log.info(username + "님이 탈퇴했습니다.");
            }else{
                return new Response("error", "탈퇴에 실패했습니다.", null);
            }
        }catch(Exception e){
            log.info("탈퇴에 실패했습니다.");
        }
        return new Response("success", "성공적으로 탈퇴했습니다.", null);
    }
}
