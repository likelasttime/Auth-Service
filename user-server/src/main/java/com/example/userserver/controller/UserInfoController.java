package com.example.userserver.controller;

import com.example.userserver.model.Member;
import com.example.userserver.model.UserInfoResponse;
import com.example.userserver.model.request.PageLimitRequest;
import com.example.userserver.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value="/user-server/admin")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping("/manage")
    public ResponseEntity<?> getUsersInfo(@Valid PageLimitRequest pageLimitRequest, Errors error){
        if(error.hasErrors()){
            String errMsg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            log.info(errMsg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
        }
        Page<Member> userList = userInfoService.getUsersInfo(pageLimitRequest.of());
        Page<UserInfoResponse> userInfoResponses = userList.map(UserInfoResponse::new);
        return ResponseEntity.ok(userInfoResponses);
    }
}
