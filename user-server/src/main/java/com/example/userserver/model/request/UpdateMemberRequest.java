package com.example.userserver.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {
    @NotBlank
    private String name;

    private String username;

    @NotBlank
    private String password;

    @Builder
    public UpdateMemberRequest(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
