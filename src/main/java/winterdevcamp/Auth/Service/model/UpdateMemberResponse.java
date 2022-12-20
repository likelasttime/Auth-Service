package winterdevcamp.Auth.Service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberResponse {
    private String name;

    private String username;

    private String email;

    @Builder
    public UpdateMemberResponse(String name, String username, String email){
        this.name = name;
        this.username = username;
        this.email = email;
    }
}
