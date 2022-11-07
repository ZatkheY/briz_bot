//package com.zatkhey.briz_bot.sevice;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@Service
//@Getter
//@Setter
//@Slf4j
//public class PingSevice {
//    @Value("${pingtask.url}")
//    private String url;
//
//    @Scheduled(fixedRateString = "${pingtask.period}")
//    public void pingMe(){
//        try {
//            URL url = new URL(getUrl());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());
//            connection.disconnect();
//        } catch (IOException e) {
//            log.error("Ping FAILED");
//            e.printStackTrace();
//        }
//    }
//}
