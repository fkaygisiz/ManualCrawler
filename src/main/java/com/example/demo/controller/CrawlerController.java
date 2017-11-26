package com.example.demo.controller;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.MainCallResult;
import com.example.demo.service.CrawlerService;

@RestController
@RequestMapping("/rest/api")
public class CrawlerController {

	private static final Logger log = LoggerFactory.getLogger(CrawlerController.class);

	@Autowired
	private CrawlerService crawlerService;

	@RequestMapping(method = RequestMethod.GET)
	public MainCallResult crawl(@RequestParam String url) {
		log.debug("url: ", url);
		try {
			URL url2 = new URL(url);
			return crawlerService.crawl(url2);
		} catch (MalformedURLException e) {
			log.error("MalformedURLException error occured. URL is " + url, e);
			MainCallResult callResult = new MainCallResult();
			callResult.setException(e.getMessage());
			return callResult;
		}
	}
}
