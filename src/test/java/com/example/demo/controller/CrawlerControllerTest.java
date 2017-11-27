package com.example.demo.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.MainCallResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerControllerTest {

	
	@Autowired
	private CrawlerController crawlerController;
	
	@Test
	public void shouldGetMalformedURLExceptionWhenMalformedUrlParamIsGiven() {
		MainCallResult crawl = crawlerController.crawl("abc");
		assertTrue(crawl.getException().equals("MalformedURLException occured"));
	}
}
