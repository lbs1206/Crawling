package com.hanil.fluxus.crawling.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class NaverMap {

    //private String Id;

    private String CCName;//골프장 명

    private String storeName;//가게명

    //private String phone;

    //private String time; //영업시간;

    //private String option;//옵션

    //private String address; //주소

    private String content; //설명

    private List<NaverMapReview> reviews; //리뷰

}
