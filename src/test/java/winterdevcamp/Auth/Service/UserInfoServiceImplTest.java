package winterdevcamp.Auth.Service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Salt;
import winterdevcamp.Auth.Service.model.UserRole;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.SaltUtil;
import winterdevcamp.Auth.Service.service.UserInfoService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class UserInfoServiceImplTest {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach(){memberRepository.deleteAll();}

    @Test
    public void getUsersInfo(){
        // given
        String salt = saltUtil.genSalt();
        Member member1 = Member.builder()
                .username("wjd00")
                .name("정")
                .email("test@gmail.com")
                .role(UserRole.ROLE_USER)
                .salt(Salt.builder().salt(salt).build())
                .password(saltUtil.encodePassword(salt, "password"))
                .build();
        memberRepository.save(member1);

        // when
        Page<Member> result = userInfoService.getUsersInfo(PageRequest.of(0, 5));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }
}
