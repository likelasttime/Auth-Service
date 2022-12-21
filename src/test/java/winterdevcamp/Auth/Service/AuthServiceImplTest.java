package winterdevcamp.Auth.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.AuthService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class AuthServiceImplTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach(){
        memberRepository.deleteAll();
    }

    @Test
    public void signUp(){
        // given
        SignUpForm signUpForm = new SignUpForm("자바", "java00", "lovejava", "java00@test.com");

        // when
        authService.signUpMember(signUpForm);

        // then
        List<Member> result = memberRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void login(){
        // given
        SignUpForm signUpForm = new SignUpForm("자바", "java00", "lovejava", "java00@test.com");

        // when
        authService.signUpMember(signUpForm);

        try{
            // when
            authService.loginMember(signUpForm.getUsername(), signUpForm.getPassword());
            // then
            log.info("로그인 성공");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void changePassword(){
        // given
        SignUpForm signUpForm = new SignUpForm("자바", "java00", "lovejava", "java00@test.com");
        authService.signUpMember(signUpForm);
        Member member = memberRepository.findByUsername("java00");

        // when
        String update_password = "newPassword";
        try {
            authService.changePassword(member, update_password);
        }catch(NotFoundException e){
            e.printStackTrace();
        }

        // then
        try{
            Member after_update = authService.loginMember(member.getUsername(), update_password);
            assertThat(after_update.getPassword()).isEqualTo(update_password);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
