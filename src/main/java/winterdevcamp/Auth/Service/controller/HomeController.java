package winterdevcamp.Auth.Service.controller;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.UpdateMemberResponse;
import winterdevcamp.Auth.Service.service.AuthService;

import java.security.Principal;

@Controller
@RequestMapping
@Slf4j
public class HomeController {
    @Autowired
    private AuthService authService;

    @GetMapping("/user/signup")
    public String signupForm(){
        return "signup";
    }

    @GetMapping("/user/login")
    public String login(){return "login";}

    @GetMapping("/user/verify")
    public String email_authentication(Principal principal, Model model){
        try {
            String username = principal.getName();
            Member member = authService.findByUsername(username);
            model.addAttribute("email", member.getEmail());
            model.addAttribute("username", username);
        }catch(Exception e){
            log.info("사용자 정보가 없습니다.");
        }
        return "authentication";
    }

    @GetMapping("/user/verify/{key}")
    public String verify(@PathVariable String key, Model model){
        model.addAttribute("key", key);
        return "verify";
    }

    @GetMapping("/user/password")
    public String requestPasswordMail(){
        return "updatePassword";
    }

    @GetMapping("/user/password/{key}")
    public String requestPassword(){
        return "passwordMail";
    }

    @GetMapping("/manage_users")
    public String getUsers(){
        return "manage/members";
    }

    @GetMapping("/user/info")
    public String updateInfo(Model model, Principal principal){
        try {
            Member member = authService.findByUsername(principal.getName());
            UpdateMemberResponse updateMemberResponse = UpdateMemberResponse.builder()
                    .name(member.getName())
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .build();
            model.addAttribute("member", updateMemberResponse);
        }catch(NotFoundException e){
            log.info("사용자 정보가 없습니다.");
        }
        return "mypage";
    }
}