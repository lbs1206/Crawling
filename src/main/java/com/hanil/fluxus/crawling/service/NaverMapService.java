package com.hanil.fluxus.crawling.service;

import com.hanil.fluxus.crawling.model.Deezer;
import com.hanil.fluxus.crawling.model.NaverMap;
import com.hanil.fluxus.crawling.model.NaverMapReview;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverMapService {

    private static String NAVER_URL = "https://map.naver.com/";


    public List<NaverMap> getNaverDatas(String cc) throws IOException {

        return getNaver(NAVER_URL,cc);

    }


    static List<NaverMap> getNaver(String naverUrl,String cc ) throws IOException {
        final File driverFile = new File("src/main/resources/bin/chromedriver.exe");
        //final File driverFile = new File("src/main/resources/bin/ec2/chromedriver");
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

        List<NaverMap> naverMaps = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor)  driver;
        try{
            driver.get(naverUrl);
            Thread.sleep(5000);
            //검색어 입력
            WebElement inputSearch = driver.findElement(By.cssSelector("input[id*='input_search']"));
            inputSearch.sendKeys(cc);

            //검색버튼 클릭
            WebElement SearchBtn = driver.findElement(By.className("button_search"));
            SearchBtn.sendKeys(Keys.ENTER);
            Thread.sleep(3000);

            //줌아웃
            WebElement ZoomOutBtn = driver.findElement(By.cssSelector(".control_widget .btn_zoom_out"));
            ZoomOutBtn.click();

            Thread.sleep(2000);

            //음식점 검색
            inputSearch.clear();
            inputSearch.sendKeys("음식점");

            SearchBtn.sendKeys(Keys.ENTER);

            Thread.sleep(3000);

            WebElement storeList = driver.switchTo().frame("searchIframe").findElement(By.cssSelector("#_pcmap_list_scroll_container ul"));

            List<WebElement> stores = storeList.findElements(By.className("_3t81n"));


            for(WebElement store : stores){

                driver.switchTo().defaultContent().switchTo().frame("searchIframe");
                WebElement storeName = store.findElement(By.className("_3Yilt"));
                storeName.click();
                String sn = storeName.getText();


                Thread.sleep(4000);

                String content = driver.switchTo().defaultContent().switchTo().frame("entryIframe").findElement(By.cssSelector(".place_section_content")).getText();

                WebElement reviewBtn = driver.findElement(By.partialLinkText("리뷰"));

                reviewBtn.click();

                Thread.sleep(2000);

                List<WebElement> reviewList = driver.findElements(By.cssSelector("._1QS0G li"));
                List<NaverMapReview> reviews = new ArrayList<>();

                for(WebElement review : reviewList){
                    NaverMapReview naverMapReview
                            = NaverMapReview.builder()
                            .review(review.getText())
                            .build();

                    reviews.add(naverMapReview);

                }

                NaverMap naverMap = NaverMap.builder()
                        .CCName(cc)
                        .content(content)
                        .storeName(sn)
                        .reviews(reviews)
                        .build();

                naverMaps.add(naverMap);
            }



        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally{
            driver.quit();
            service.stop();
        }


        return naverMaps;
    }

}
