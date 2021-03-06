package com.hanil.fluxus.crawling.controller;

import com.hanil.fluxus.crawling.model.Deezer;
import com.hanil.fluxus.crawling.model.JohnsStats;
import com.hanil.fluxus.crawling.model.KoreaStats;
import com.hanil.fluxus.crawling.model.NaverMap;
import com.hanil.fluxus.crawling.service.CovidService;
import com.hanil.fluxus.crawling.service.DeezerService;
import com.hanil.fluxus.crawling.service.NaverMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrawlingApiController {

    @Autowired
    private CovidService covidService;

    @Autowired
    private DeezerService deezerService;

    @Autowired
    private NaverMapService naverMapService;

    @PostMapping("/api/crawling/test")
    public List<KoreaStats> getKoreaCovidDatas() throws IOException {

        return covidService.getKoreaCovidDatas();

    }

    @PostMapping("/api/crawling/johns")
    public List<JohnsStats> getJohnsCovidDatas() throws IOException{

        return covidService.getJhonsCovidDatas();

    }

    @PostMapping("/api/crawling/deezer")
    public List<Deezer> getDeezerDatas() throws IOException{

        return deezerService.getDeezerDatas("");

    }

    @PostMapping("/api/crawling/deezer/{upc}")
    public List<Deezer> getDeezerDatas(@PathVariable String upc) throws IOException{

        return deezerService.getDeezerDatas(upc);

    }

    @PostMapping("/api/crawling/naver")
    public List<NaverMap> getNaverMapDatas() throws IOException{

        return naverMapService.getNaverDatas("");

    }



    @PostMapping("/api/crawling/naver/{cc}")
    public List<NaverMap> getNaverMapDatas(@PathVariable String cc) throws IOException{

        return naverMapService.getNaverDatas(cc);

    }



}
