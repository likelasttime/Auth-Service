package winterdevcamp.Auth.Service.controller.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import winterdevcamp.Auth.Service.model.request.SignUpForm;
import winterdevcamp.Auth.Service.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz){
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors){
        SignUpForm signUpForm = (SignUpForm) target;
        if(memberRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 이메일입니다.");
        }
        if(memberRepository.existsByUsername(signUpForm.getUsername())){
            errors.rejectValue("username", "invalid.username", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 아이디입니다.");
        }
    }
}
