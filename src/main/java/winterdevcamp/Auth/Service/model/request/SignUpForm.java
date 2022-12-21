package winterdevcamp.Auth.Service.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    @NotBlank
    @Length(min = 2, max = 10)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z]{2,10}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9_-]{3,20}$")
    private String username;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Email
    private String email;
}
