package com.example.userserver.controller;

import com.example.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="/user-server/user/")
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @GetMapping("/info")
    public String updateInfo(){
        /*try {
            Member member = userService.findByUsername(principal.getName());
            UpdateMemberResponse updateMemberResponse = UpdateMemberResponse.builder()
                    .name(member.getName())
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .build();
            model.addAttribute("member", updateMemberResponse);
        }catch(Exception e){
            log.info("사용자 정보가 없습니다.");
        }*/
        return "mypage";
    }
}
