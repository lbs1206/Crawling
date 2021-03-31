package com.hanil.fluxus.crawling.controller;

import com.hanil.fluxus.crawling.model.KoreaStats;
import com.hanil.fluxus.crawling.service.CovidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class CovidController {

    @Autowired
    private CovidService covidService;


    @GetMapping(value="/covid/korea")
    public String korea(Model model) throws IOException {

        List<KoreaStats> koreaStatsList = covidService.getKoreaCovidDatas();

        model.addAttribute("koreaStats",koreaStatsList);

        return "covid/korea";
    }
}
