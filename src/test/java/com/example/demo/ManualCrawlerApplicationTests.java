package com.example.demo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.controller.CrawlerController;
import com.example.demo.model.MainCallResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualCrawlerApplicationTests {

	@Autowired
	private CrawlerController crawlerController;
	
	@Test
	public void contextLoads() {
		assertNotNull(crawlerController);
	}
	
	@Test
	public void shouldGetMainCallResultFromGithubURL() throws MalformedURLException {
		MainCallResult mainCallResult = crawlerController.crawl("https://github.com/login");
		assertTrue(mainCallResult.getInternalLinks().size() > 0);
		assertTrue(mainCallResult.getExternalLinks().size() > 0);
		assertTrue(mainCallResult.getTitle().equals("Sign in to GitHub · GitHub"));
		assertTrue(mainCallResult.isHasLogin());
	}

}
