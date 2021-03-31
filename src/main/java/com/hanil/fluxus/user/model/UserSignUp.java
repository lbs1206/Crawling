package com.hanil.fluxus.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignUp {

    private String userid;
    private String username;
    private String password;

    public User toEntity() {
        return User.builder()
                .userid(userid)
                .username(username)
                .password(password)
                .build();
    }



    @Builder
    public UserSignUp(String userid,String username, String password) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }

}
