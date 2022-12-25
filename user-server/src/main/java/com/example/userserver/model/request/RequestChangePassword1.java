package com.example.userserver.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChangePassword1 {
    private String username;
    private String email;
}
