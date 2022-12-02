package com.crawling.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Crawling API")
@CrossOrigin("*")
public class CrawlingController {

	@ApiOperation(value = "크롤링", notes = "")
	@GetMapping(value = "/crawling/blog")
	public ResponseEntity<?> blog() throws IOException {
		List<HashMap<String, String>> blogList = new ArrayList<>();

		String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EB%B6%80%EB%8F%99%EC%82%B0";
		Document doc = null;

		try {
			doc = Jsoup.connect(url).get();
			Elements ele = doc.select(".total_wrap.api_ani_send");
			Elements titleEle = ele.select(".api_txt_lines.total_tit._cross_trigger");
			Elements titleHrefEle = ele.select(".api_txt_lines.total_tit._cross_trigger");
			Elements imgSrcEle = ele.select(".thumb.api_get");
			Elements textEle = ele.select(".api_txt_lines.dsc_txt");
			for (int i = 0; i < 3; i++) {
				HashMap<String, String> map = new HashMap<>();
				
				String title = titleEle.get(i).text();
				String titleHref = titleHrefEle.get(i).attr("href");
				String imgSrc = imgSrcEle.get(i).attr("src");
				String text = textEle.get(i).text();
				
				map.put("title", title);
				map.put("titleHref", titleHref);
				map.put("imgSrc", imgSrc);
				map.put("text", text);
				
				blogList.add(map);
			}

			if (blogList.size() != 0) {
				return new ResponseEntity<List<HashMap<String, String>>>(blogList, HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "크롤링", notes = "")
	@GetMapping(value = "/crawling/news")
	public ResponseEntity<?> news() throws IOException {
		List<HashMap<String, String>> newsList = new ArrayList<>();
		
		String url = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=101&sid2=260";
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
			Elements ele = doc.select("dl");
			
			for (int i = 0; i < 5; i++) {
				HashMap<String, String> map = new HashMap<>();
				
				String title = ele.get(i).select("a").text();
				String titleHref = ele.get(i).select("a").attr("href");
				String text = ele.get(i).select("span.lede").text();

//				System.out.println(title);
//				System.out.println(titleHref);
//				System.out.println(text);
				
				map.put("title", title);
				map.put("titleHref", titleHref);
				map.put("text", text);
				
				newsList.add(map);
			}
			
			if (newsList.size() != 0) {
				return new ResponseEntity<List<HashMap<String, String>>>(newsList, HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

}
