package winterdevcamp.Auth.Service.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {
    private String name;

    private String username;

    private String password;

    @Builder
    public UpdateMemberRequest(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
