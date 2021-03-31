package com.hanil.fluxus.crawling.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class JohnsStats {

    private String country; // 나라

    private String cases; //감여자

    private String deaths; //사망자

    private String recovared; //회복

    //private int total; // 합계

}
