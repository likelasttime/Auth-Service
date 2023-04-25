package winterdevcamp.Auth.Service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import winterdevcamp.Auth.Service.model.Member;
import winterdevcamp.Auth.Service.model.UserRole;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.SaltUtil;
import winterdevcamp.Auth.Service.service.UserInfoServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserInfoServiceImplTest {
    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    @Mock
    private SaltUtil saltUtil;

    @Mock
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach(){memberRepository.deleteAll();}

    @Test
    public void getUsersInfo(){
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Member member = Member.builder()
                .username("user")
                .password("password")
                .build();
        List<Member> members = new ArrayList<>();
        members.add(member);
        when(memberRepository.findAllByRoleNotLike(UserRole.ROLE_NOT_PERMITTED, pageable))
                .thenReturn(new PageImpl<Member>(members));

        // when
        Page<Member> result = userInfoService.getUsersInfo(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }
}
