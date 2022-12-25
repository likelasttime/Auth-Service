package com.example.userserver.controller;

import com.example.userserver.model.Member;
import com.example.userserver.model.Response;
import com.example.userserver.model.request.RequestChangePassword1;
import com.example.userserver.model.request.RequestChangePassword2;
import com.example.userserver.model.request.UpdateMemberRequest;
import com.example.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequestMapping(value="/user-server/user/")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

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
