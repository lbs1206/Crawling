package com.hanil.fluxus.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.hanil.fluxus.user.model.User;
import com.hanil.fluxus.user.model.UserLoginToken;
import com.hanil.fluxus.user.model.UserSignUp;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;
import com.auth0.jwt.JWT;

@UtilityClass
public class JWTUtils {

    private static String KEY = "hanil";
    private static final String CLAIM_USER_ID = "user_id";

    //jwt 토큰 생성
    public static UserLoginToken createToken(UserSignUp user) {

        if (user == null) {
            return null;
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim(CLAIM_USER_ID, user.getUserid())
                .withSubject(user.getUsername())
                .withIssuer(user.getUserid())
                .sign(Algorithm.HMAC512(KEY.getBytes()));

        return UserLoginToken.builder()
                .token(token)
                .build();
    }

    //jwt 토큰에서  이메일추출
    public static String getIssuer(String token) {

        String issuer = JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token)
                .getIssuer();

        return issuer;

    }

    //jwt 토큰에서  이름추출
    public static String getSubject(String token) {

        String issuer = JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        return issuer;

    }


}
