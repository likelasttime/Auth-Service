package winterdevcamp.Auth.Service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/user/signupform")
    public String signupForm(){
        return "/signup";
    }

    @GetMapping("/login")
    public String login(){return "/login";}
}
