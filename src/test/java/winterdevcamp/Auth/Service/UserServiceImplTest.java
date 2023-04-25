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
import winterdevcamp.Auth.Service.service.SaltUtil;
import winterdevcamp.Auth.Service.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private SaltUtil saltUtil;

    private static Member member;

    @BeforeAll
    public static void beforeAll(){
        member = Member.builder()
                .username("user")
                .password("password")
                .salt(new Salt("1"))
                .build();
    }

    @Test
    public void isPasswordEqual(){
        // given
        final String requestPwd = "password";
        when(memberRepository.findByUsername(member.getUsername())).thenReturn(member);
        when(saltUtil.encodePassword(member.getSalt().getSalt(), requestPwd)).thenReturn(member.getPassword());

        // when
        boolean result = userService.isPasswordEqual(member.getUsername(), requestPwd);

        // then
        assertTrue(result);
    }
}
