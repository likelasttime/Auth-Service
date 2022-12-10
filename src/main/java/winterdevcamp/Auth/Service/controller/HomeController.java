package winterdevcamp.Auth.Service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.service.AuthService;

import java.security.Principal;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private AuthService authService;

    @GetMapping("/user/signupform")
    public String signupForm(){
        return "/signup";
    }

    @GetMapping("/login")
    public String login(){return "/login";}

    @GetMapping("/authentication")
    public String email_authentication(Principal principal, Model model){
        String username = principal.getName();
        try {
            Member member = authService.findByUsername(username);
            model.addAttribute("email", member.getEmail());
            model.addAttribute("username", username);
        }catch(Exception e){
            log.info("사용자 정보가 없습니다.");
        }
        return "/authentication";
    }
}