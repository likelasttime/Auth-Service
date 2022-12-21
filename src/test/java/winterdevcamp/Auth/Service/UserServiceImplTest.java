package winterdevcamp.Auth.Service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.model.request.UpdateMemberRequest;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.AuthService;
import winterdevcamp.Auth.Service.service.UserService;

@Slf4j
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @AfterEach
    public void afterEach(){
        memberRepository.deleteAll();
    }

    @Test
    public void updateMemberInfo(){
        // given
        SignUpForm signUpForm = new SignUpForm("자바", "java00", "lovejava", "java00@test.com");
        Member member = memberRepository.findByUsername("java00");

        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .username("wjd00")
                .password("newpwd")
                .name("개명")
                .build();

        authService.signUpMember(signUpForm);
        try {
            userService.updateMemberInfo(request);

            // then
            Member after = memberRepository.findByUsername("wjd00");
            Assertions.assertThat(after.getName()).isEqualTo("개명");
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
