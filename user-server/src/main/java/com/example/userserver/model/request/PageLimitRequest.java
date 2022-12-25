package com.example.userserver.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;

@Getter
@Setter
public class PageLimitRequest {
    private int page;

    @Max(value=100, message="요청할 수 있는 size의 최대크기는 100입니다.")
    private int size;

    private Sort.Direction sortDirection;

    private String sortColumn;

    public void setPage(int page){
        this.page=page <= 0 ? 1 : page;
    }

    public PageRequest of(){
        return PageRequest.of(page - 1, size, sortDirection, sortColumn);
    }
}
