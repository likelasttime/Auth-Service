package winterdevcamp.Auth.Service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import winterdevcamp.Auth.Service.model.Response;
import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.repository.MemberRepository;
import winterdevcamp.Auth.Service.service.AuthService;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    @AfterEach
    public void afterEach(){memberRepository.deleteAll();}

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

        // then
        assertTrue(memberRepository.existsByEmail("java@test.com"));
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
        SignUpForm signUpForm = new SignUpForm("자바", "user", "password", "java00@test.com");
        authService.signUpMember(signUpForm);
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "password");
        String request = objectMapper.writeValueAsString(map);

        // when
        mockMvc.perform(post("/user/remove")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(memberRepository.findAll().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("회원 탈퇴 : 비밀번호 다름")
    @WithMockUser
    void removeMemberWithError() throws Exception{
        // given
        SignUpForm signUpForm = new SignUpForm("자바", "user", "password", "java00@test.com");
        authService.signUpMember(signUpForm);
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "notpassword");
        String request = objectMapper.writeValueAsString(map);

        // when
        Response response = new Response("error", "탈퇴에 실패했습니다.", null);
        mockMvc.perform(post("/user/remove")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(memberRepository.findAll().size()).isEqualTo(1);

    }
}
