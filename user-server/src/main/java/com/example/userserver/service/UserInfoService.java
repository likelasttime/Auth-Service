package com.example.userserver.service;

import com.example.userserver.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInfoService {
    Page<Member> getUsersInfo(Pageable pageable);
}
