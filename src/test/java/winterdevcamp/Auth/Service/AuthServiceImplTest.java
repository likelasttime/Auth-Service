package winterdevcamp.Auth.Service;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.Salt;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.AuthServiceImpl;
import winterdevcamp.Auth.Service.service.SaltUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SaltUtil saltUtil;

    private static Member member;

    @BeforeAll
    public static void beforeAll() {
        member = Member.builder()
                .username("user")
                .password("password")
                .salt(new Salt("1"))
                .build();
    }

    @Test
    public void login() throws Exception {
        // given
        when(memberRepository.findByUsername(member.getUsername())).thenReturn(member);
        when(saltUtil.encodePassword(member.getSalt().getSalt(), member.getPassword()))
                .thenReturn("password");

        // when
        Member result = authService.loginMember(member.getUsername(), member.getPassword());

        // then
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getUsername()).isEqualTo(member.getUsername());
    }
}
