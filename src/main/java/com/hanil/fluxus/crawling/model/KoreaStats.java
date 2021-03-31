package com.hanil.fluxus.crawling.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class KoreaStats {

    private String country; // 시도명

    private int l_total; // 합계

    private int l_local; //국내 발생

    private int l_inflow; // 해외 유입

    private int total_patient; // 확진환자수

    private int quarantine; // 격리중

    private int quarantine_release; // 격리 해제

    private int death; // 사망자수

    private double incidence; // 발병률

}