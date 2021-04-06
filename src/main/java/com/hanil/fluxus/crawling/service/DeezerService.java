package com.hanil.fluxus.crawling.service;

import com.hanil.fluxus.crawling.model.Deezer;
import com.hanil.fluxus.crawling.model.JohnsStats;
import com.hanil.fluxus.crawling.model.KoreaStats;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class DeezerService {
    private static String DEEZER_URL = "https://backstage.deezer.com/";


    public List<Deezer> getDeezerDatas(String upc) throws IOException {

        return getDeezer(DEEZER_URL,upc);

    }

    static List<Deezer> getDeezer(String deezerUrl,String upc ) throws IOException {
        //final File driverFile = new File("src/main/resources/bin/chromedriver.exe");
        final File driverFile = new File("src/main/resources/bin/ec2/chromedriver");
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

        List<Deezer> deezerList = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor)  driver;
        try{
            driver.get(deezerUrl);
            Thread.sleep(1000);

            //로그인 버튼 클릭
            WebElement loginBtn = driver.findElement(By.cssSelector(".Header_button__1kcCz"));
            js.executeScript("arguments[0].click();",loginBtn);
            Thread.sleep(2000);
            //로그인 정보 입력
            List<WebElement> loginList = driver.findElements(By.cssSelector(".TextInput_input__1izzF"));

            loginList.get(0).sendKeys("");
            loginList.get(1).sendKeys("");

            Thread.sleep(2000);
            //sign in click
            WebElement loginSubmit = driver.findElement(By.cssSelector(".Button_button--primary__V2BoS"));
            loginSubmit.submit();

            Thread.sleep(5000);

            WebElement searchBtn = driver.findElement(By.cssSelector(".SearchBar_searchButton__3A4Q2"));
            searchBtn.click();

            Thread.sleep(2000);

            //검색
            WebElement search = driver.findElement(By.cssSelector(".SearchBar_searchControl__2tYX7"));
            search.sendKeys("191953119120");

            Thread.sleep(2000);

            String temp = driver.findElement(By.cssSelector(".SearchData_informations__2qH21")).getText();
            String tempId = temp.split(":")[1].trim().replace("UPC","").replace("\n","");


            Deezer deezer = Deezer.builder()
                    .deezerId(tempId)
                    .upc(upc)
                    .build();

            deezerList.add(deezer);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally{
            driver.quit();
            service.stop();
        }


        return deezerList;
    }



}
