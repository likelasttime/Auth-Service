package winterdevcamp.Auth.Service.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestLoginMember {
    private String username;
    private String password;

    @Builder
    public RequestLoginMember(String username, String password){
        this.username = username;
        this.password = password;
    }
}
