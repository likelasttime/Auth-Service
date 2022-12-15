package winterdevcamp.Auth.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.AuthService;

import java.util.List;

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
        Member member = Member.builder()
                .username("wjd00")
                .password("helloworld")
                .name("정")
                .email("test@gmail.com")
                .build();

        // when
        authService.signUpMember(member);

        // then
        List<Member> result = memberRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void login(){
        // given
        Member member = Member.builder()
                .username("wjd00")
                .password("helloworld")
                .name("정")
                .email("test@gmail.com")
                .build();

        authService.signUpMember(member);

        try{
            // when
            authService.loginMember(member.getUsername(), member.getPassword());
            // then
            log.info("로그인 성공");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void changePassword(){
        // given
        Member member = Member.builder()
                .username("wjd00")
                .password("helloworld")
                .name("정")
                .email("test@gmail.com")
                .build();
        authService.signUpMember(member);

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
            Assertions.assertThat(after_update.getPassword()).isEqualTo(update_password);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
