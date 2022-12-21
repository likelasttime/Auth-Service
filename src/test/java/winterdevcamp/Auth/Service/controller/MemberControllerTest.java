package winterdevcamp.Auth.Service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .andExpect(content().string("must not be blank"))
                .andExpect(status().is(400));
    }
}
