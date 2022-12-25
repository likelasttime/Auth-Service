package com.example.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Salt {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String salt;

    @Builder
    public Salt(String salt){
        this.salt = salt;
    }
}
