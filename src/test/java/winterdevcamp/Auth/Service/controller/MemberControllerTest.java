package winterdevcamp.Auth.Service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import winterdevcamp.Auth.Service.config.JwtRequestFilter;
import winterdevcamp.Auth.Service.controller.validation.SignUpFormValidator;
import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.*;

import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class, WebSecurityConfigurerAdapter.class})
        })
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    AuthService authService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    CookieUtil cookieUtil;

    @MockBean
    RedisUtil redisUtil;

    @MockBean
    UserService userService;

    @MockBean
    SignUpFormValidator signUpFormValidator;

    @Test
    @DisplayName("회원 가입 처리 : 입력값 정상")
    void signUpSubmit() throws Exception{
        // given
        String signupForm = objectMapper.writeValueAsString(new SignUpForm("자바", "java00", "hellojava", "java@test.com"));

        // when
        mockMvc.perform(post("/user/signup")
                .content(signupForm)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 처리 : 입력값 오류")
    void signUpSubmitWithError() throws Exception{
        // given
        String signupForm = objectMapper.writeValueAsString(new SignUpForm("", "java00", "hellojava", "java@test.com"));

        // when
        mockMvc.perform(post("/user/signup")
                        .content(signupForm)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("회원 탈퇴 : 비밀번호 동일")
    @WithMockUser
    void removeMember() throws Exception{
        // given
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "password");
        String request = objectMapper.writeValueAsString(map);

        // when
        mockMvc.perform(post("/user/remove")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        jsonPath("$.response").value("success"),
                        jsonPath("$.message").value("성공적으로 탈퇴했습니다."));
    }
}