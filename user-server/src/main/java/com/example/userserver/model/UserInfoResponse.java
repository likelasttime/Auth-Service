package com.example.userserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserInfoResponse {

    private String name;

    private String username;

    private String email;

    private UserRole role;

    private Date createAt;

    @Builder
    public UserInfoResponse(Member member) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.createAt = member.getCreateAt();
    }
}
