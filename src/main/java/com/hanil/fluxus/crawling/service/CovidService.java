package com.hanil.fluxus.crawling.service;

import com.hanil.fluxus.crawling.model.JohnsStats;
import com.hanil.fluxus.crawling.model.KoreaStats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CovidService {

    private static String COVID_URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun=";

    private static String JHONS_URL = "https://gisanddata.maps.arcgis.com/apps/opsdashboard/index.html#/bda7594740fd40299423467b48e9ecf6";


    public List<KoreaStats> getKoreaCovidDatas() throws IOException {

        return getKoreaStats(COVID_URL);

    }

    static List<KoreaStats> getKoreaStats(String covidUrl) throws IOException {
        List<KoreaStats> koreaStatsList = new ArrayList<>();
        Document doc = Jsoup.connect(covidUrl).get();

        Elements contents = doc.select("table tbody tr");

        for(Element content : contents){
            Elements tdContents = content.select("td");

            KoreaStats koreaStats = KoreaStats.builder()
                    .country(content.select("th").text())
                    .l_total(Integer.parseInt(tdContents.get(0).text().replace(",","")))
                    .l_local(Integer.parseInt(tdContents.get(1).text().replace(",","")))
                    .l_inflow(Integer.parseInt(tdContents.get(2).text().replace(",","")))
                    .total_patient(Integer.parseInt(tdContents.get(3).text().replace(",","")))
                    .quarantine(Integer.parseInt(tdContents.get(4).text().replace(",","")))
                    .quarantine_release(Integer.parseInt(tdContents.get(5).text().replace(",","")))
                    .death(Integer.parseInt(tdContents.get(6).text().replace(",","")))
                    .incidence(Double.parseDouble(tdContents.get(7).text().replace("-","0")))
                    .build();

            koreaStatsList.add(koreaStats);
        }

        return koreaStatsList;
    }



    public List<JohnsStats> getJhonsCovidDatas() throws IOException {

        return getJhonsStats(JHONS_URL);

    }

    static List<JohnsStats> getJhonsStats(String covidUrl) throws IOException {
        final File driverFile = new File("src/main/resources/bin/chromedriver.exe");
        final String driverFilePath = driverFile.getAbsolutePath();
        if(!driverFile.exists() && driverFile.isFile()) {
            throw new RuntimeException("Not found file. or this is not file. <" + driverFilePath + ">");
        }

        final ChromeDriverService service;
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(driverFile)
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final WebDriver driver = new ChromeDriver(service);
        final WebDriverWait wait = new WebDriverWait(driver, 10);

        List<JohnsStats> johnsStatsList = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor)  driver;
        try{
            driver.get(covidUrl);
            Thread.sleep(10000);

            List<WebElement> contents = driver.findElements(By.cssSelector("#ember35 .feature-list .ember-view"));
            Map<String,Object> contentMap = new HashMap<>();
            Map<String,Object> recoverMap = new HashMap<>();
            Map<String,Object> deathMap = new HashMap<>();

            for(WebElement content : contents){


                List<WebElement> tdContents = content.findElements(By.tagName("span"));

                contentMap.put(tdContents.get(2).getText(),tdContents.get(0).getText());
                recoverMap.put(tdContents.get(2).getText(),"0");

            }


            WebElement recoverBtn = driver.findElement(By.id("ember233"));
            //recoverBtn.click();
            Thread.sleep(2000);
            js.executeScript("arguments[0].click();",recoverBtn);

            List<WebElement> recoverList = driver.findElements(By.cssSelector("#ember115 .feature-list .ember-view"));


            for(WebElement recover : recoverList){
                List<WebElement> tdRecover = recover.findElements(By.tagName("span"));

                recoverMap.put(tdRecover.get(1).getText(),tdRecover.get(0).getText());
            }


            WebElement deathBtn = driver.findElement(By.id("ember230"));
            js.executeScript("arguments[0].click();",deathBtn);
            Thread.sleep(2000);
            List<WebElement> deathList = driver.findElements(By.cssSelector("#ember101 .feature-list .ember-view"));


            for(WebElement death : deathList){

                List<WebElement> tdDeath = death.findElements(By.tagName("span"));

                deathMap.put(tdDeath.get(1).getText(),tdDeath.get(0).getText());



                JohnsStats johnsStats = JohnsStats.builder()
                        .country(tdDeath.get(1).getText())
                        .cases(contentMap.get(tdDeath.get(1).getText()).toString())
                        .deaths(tdDeath.get(0).getText())
                        .recovared(recoverMap.get(tdDeath.get(1).getText()).toString())
                        .build();

                johnsStatsList.add(johnsStats);


            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally{
            driver.quit();
            service.stop();
        }


        return johnsStatsList;
    }

}
