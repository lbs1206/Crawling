package com.hanil.fluxus.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@NoArgsConstructor
public class User {

    private String userid;
    private String username;
    private String password;

    @Builder
    public User(String userid,String username, String password) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }


}
