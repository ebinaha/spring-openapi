package com.example.openapi.controller;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.html.UrlCssBuilder;

@RestController
public class OpenApiController {
	
	@GetMapping("/weather")
	public String weather() {
		try {
			String servicekey = "HcKKeysAdTw4YkxmGY5%2FjKi%2BehZGp%2BUd0pYQeH3A0jL%2BeQ%2Fn15oOVxND9xY7ZeHfbIdV5PXlnd%2Fe5hb74idbkw%3D%3D";
			// 초기값(URL)을 가지고 생성할 수도 있음
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst"); /*URL*/
			
			// api가 정해놓은 key
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" +servicekey); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        // XML -> JSON 형식으로 변경 
	        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
	        urlBuilder.append("&" + URLEncoder.encode("stnId","UTF-8") + "=" + URLEncoder.encode("108", "UTF-8")); /*108 전국, 109 서울, 인천, 경기도 등 (활용가이드 하단 참고자료 참조)*/
	        // 오늘 날짜 기준으로 날짜 변경 
	        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode("202211300600", "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력 YYYYMMDD0600 (1800)-최근 24시간 자료만 제공*/
	        URL url = new URL(urlBuilder.toString());
	        
	        // Jsoup의 Connection과 다름, Java에서 제공하는 connection 
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        System.out.println("Response code: " + conn.getResponseCode());
	        
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	        	// 문자단위(보조) - 중간(보조) - byte단위 
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        StringBuilder sb = new StringBuilder();
	        String line;
	        // line 단위로 읽음, 더이상 데이터가 없으면 null 반환 
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        // stream을 사용하고 난 뒤 close 필수 
	        rd.close();
	        conn.disconnect();
	        System.out.println(sb.toString());
	        return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}
	

}
